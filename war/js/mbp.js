function Map(linkItems) {
    this.current = undefined;
    this.size = 0;

    if(linkItems === false)
        this.disableLinking();
}

Map.noop = function() {
    return this;
};

Map.illegal = function() {
    throw new Error("illegal operation for maps without linking");
};

// map initialisation from existing object
// doesn't add inherited properties if not explicitly instructed to:
// omitting foreignKeys means foreignKeys === undefined, i.e. == false
// --> inherited properties won't be added
Map.from = function(obj, foreignKeys) {
    var map = new Map;

    for(var prop in obj) {
        if(foreignKeys || obj.hasOwnProperty(prop))
            map.put(prop, obj[prop]);
    }

    return map;
};

Map.prototype.disableLinking = function() {
    this.link = Map.noop;
    this.unlink = Map.noop;
    this.disableLinking = Map.noop;
    this.next = Map.illegal;
    this.key = Map.illegal;
    this.value = Map.illegal;
    this.removeAll = Map.illegal;

    return this;
};

// overwrite in Map instance if necessary
Map.prototype.hash = function(value) {
    return (typeof value) + ' ' + (value instanceof Object ?
        (value.__hash || (value.__hash = ++arguments.callee.current)) :
        value.toString());
};

Map.prototype.hash.current = 0;

// --- mapping functions

Map.prototype.get = function(key) {
    var item = this[this.hash(key)];
    return item === undefined ? undefined : item.value;
};

Map.prototype.put = function(key, value) {
    var hash = this.hash(key);

    if(this[hash] === undefined) {
        var item = { key : key, value : value };
        this[hash] = item;

        this.link(item);
        ++this.size;
    }
    else this[hash].value = value;

    return this;
};

Map.prototype.remove = function(key) {
    var hash = this.hash(key);
    var item = this[hash];

    if(item !== undefined) {
        --this.size;
        this.unlink(item);

        delete this[hash];
    }

    return this;
};

// only works if linked
Map.prototype.removeAll = function() {
    while(this.size)
        this.remove(this.key());

    return this;
};

// --- linked list helper functions

Map.prototype.link = function(item) {
    if(this.size == 0) {
        item.prev = item;
        item.next = item;
        this.current = item;
    }
    else {
        item.prev = this.current.prev;
        item.prev.next = item;
        item.next = this.current;
        this.current.prev = item;
    }
};

Map.prototype.unlink = function(item) {
    if(this.size == 0)
        this.current = undefined;
    else {
        item.prev.next = item.next;
        item.next.prev = item.prev;
        if(item === this.current)
            this.current = item.next;
    }
};

// --- iterator functions - only work if map is linked

Map.prototype.next = function() {
    this.current = this.current.next;
};

Map.prototype.key = function() {
    return this.current.key;
};

Map.prototype.value = function() {
    return this.current.value;
};

function isMobile() {
  var mobile = (/iphone|ipad|ipod|android|blackberry|mini|windows\sce|palm/i.test(navigator.userAgent.toLowerCase()));
  if (mobile) {
    return true;
    } else {
    return false;
  }
}
function detectBrowser() {

  var useragent = navigator.userAgent;
  var mapdiv = document.getElementById("map_canvas");

  if (useragent.indexOf('iPhone') != -1 || useragent.indexOf('Android') != -1 ) {
    mapdiv.style.width = '100%';
    mapdiv.style.height = '100%';
  } else {
    mapdiv.style.width = '600px';
    mapdiv.style.height = '800px';
  }
}

function handleNoGeolocation(errorFlag) {
 if (errorFlag) {
      var content = 'Error: The Geolocation service failed.';
    } else {
      var content = 'Error: Your browser doesn\'t support geolocation.';
    }

    var options = {
      map: map,
      position: new google.maps.LatLng(51.51537,-0.14188),
      content: content
    };

   // var infowindow = new google.maps.InfoWindow(options);
    map.setCenter(options.position);
}

function findDistance(cLat,cLon,tLat,tLon) {
  var lat = Math.pow((cLat - tLat),2);
  var lon = Math.pow((cLon - tLon),2);
  return Math.sqrt(lat+lon);
}
function presetCities() {
  var cities = [{name: "NewYork", lat: 40.7512, lon: -73.9885},
                {name: "London", lat: 51.50832, lon: -0.12774},
                {name: "SanFrancisco", lat: 37.7770, lon: -122.4176},
                {name: "Boston", lat: 42.3592, lon: -71.0591},
                {name: "Chicago", lat: 41.8789, lon: -87.6291}];
  return cities;
}
function findNearestCity(position) {
  
    
    var pos = { lat: position.coords.latitude,
                lon: position.coords.longitude};
    
    var cities = presetCities();
    var closestCity;
    for (var i=0; i<cities.length;i++) {
      var distance = findDistance(pos.lat,pos.lon,cities[i].lat,cities[i].lon);
      if (i==0) {
        minDistance = distance;
        closestCity = cities[i];
      } else if (distance < minDistance) {
        minDistance = distance;
        closestCity = cities[i];
      }
    }
    return closestCity;
    

}

function initialize() {
    var myOptions = {
      zoom: 15,
      panControl: false,
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      mapTypeControl: true,
      mapTypeControlOptions: {
        style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
        position: google.maps.ControlPosition.TOP_LEFT
      },
    };
    map = new google.maps.Map(document.getElementById('map_canvas'),
        myOptions);

    // Try HTML5 geolocation
    if(navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position) {
        var city = findNearestCity(position);
        var pos = new google.maps.LatLng(city.lat,city.lon);
        $(".city").removeClass('active');
        $("#link"+city.name).addClass('active');

       /* var infowindow = new google.maps.InfoWindow({
          map: map,
          position: pos,
          content: 'Location found using HTML5.'
        });*/

        map.setCenter(pos);
      }, function() {
        handleNoGeolocation(true);
      });
    } else {
      // Browser doesn't support Geolocation
      handleNoGeolocation(false);
    }
    google.maps.event.addListener(map, 'idle', function() {
      if (map.zoom > 13) {
        getVenues(getBounds());
        $('#alertWindow').text('');
        $('#alertWindow').hide();
      } else {
        $('#alertWindow').text('Zoom in to See Venues! Too many to load...');
        $('#alertWindow').fadeIn();
      }
    }); 
    
    
    var cities = presetCities();
    
    
    $(".city").click(function() {
      var city = $(this).data("city");
      for (var i=0; i<cities.length;i++) {
        if (cities[i].name === city){
          map.setCenter(new google.maps.LatLng(cities[i].lat, cities[i].lon));
        }
       }
      $(".city").removeClass('active');
      $(this).addClass('active');
    })
}


var markerDB = new Map;

function isOkToAdd(id) {
  if (markerDB.get(id)) { 
    return false
    } else {
    markerDB.put(id, 1);
    return true;
  }
}

function niceInfo(title,link,id) {
  var x = '<div id="title">'+title+'</div>';
  var z = '<div id="events_'+id+'">Loading...</div>';
  var l = '<div id="link"><a href="'+link+'" target="_blank">About the Venue</a></div>';
  // TODO: Attach the following to link click above
  // _gaq.push(['_trackEvent', 'Venue', 'Click', title]);
  //console.log("building box for "+ id);
  return x+z+l;
}
function addMarker(map,lat,lon,title,id,link){
//var poz  = new google.maps.LatLng(51.51376,-0.14364);
  var poz  = new google.maps.LatLng(lat,lon);
    var marker = new google.maps.Marker({
    	position: poz,
    	map:map,
    	title: title});
 
    marker.setMap(map);
  
    var infowindow = new google.maps.InfoWindow({maxWidth:200, content: niceInfo(title,link,id)});
    google.maps.event.addListener(marker, 'click', function() {
      infowindow.open(map,marker);
      _gaq.push(['_trackEvent', 'Info', 'Show']);
    });
    google.maps.event.addListener(infowindow,'domready',function(){
      getEvents({id:id},jQuery('#events_'+id));
    });
}


function getEvents(venueId,div) {
  jQuery.getJSON( '/events', venueId,function(a){
    var events = a.resultsPage.results.event;
    //console.log(events.length);
    //console.log(events);
	var thisHtml = '';
    for (var i=0; i<events.length;i++) {
	//console.log(div);
	thisHtml += '<a href="'+events[i].uri+'" target="_blank">'+events[i].displayName+'</a>';
	// TODO: Attach the following to link click above
  // _gaq.push(['_trackEvent', 'Event', 'Click', title]);
    }
	div.html(thisHtml);
  })
    _gaq.push(['_trackEvent', 'Events', 'Get']);
}

function getVenues(bounds) {
  jQuery.getJSON( '/venues', bounds,function(a){
    for (var i=0;i<a.length;i++) {
      var e = a[i];
      if (isOkToAdd(e.id))
      addMarker(map,e.lat,e.lng,e.displayName,e.id,e.uri);
      }
    })
    _gaq.push(['_trackEvent', 'Venue', 'Get']);
}
function getBounds(){
  var bounds = { maxLat:map.getBounds().getNorthEast().lat(),
      maxLon:map.getBounds().getNorthEast().lng(),
      minLat:map.getBounds().getSouthWest().lat(),
      minLon:map.getBounds().getSouthWest().lng()}
 return bounds;
}
function afterRendering() {
  if (!isMobile()) {
    $('#forkMe').show();
  }
}

var map;
google.maps.event.addDomListener(window, 'load', initialize);
$(document).ready(afterRendering)
