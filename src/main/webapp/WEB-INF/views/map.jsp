<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Locate you in the map</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <script src="https://api-maps.yandex.ru/2.1/?lang=en_US" type="text/javascript"></script>
    <script src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript">
        showlocation();

        ymaps.ready(init);
        var myMap, myPlacemark;
        var latitude = $("#latitude").val();
        var longitude = $("#longitude").val();
        console.log(latitude);
        console.log(longitude);
        function init() {
            myMap = new ymaps.Map("map", {
                center: [40.2000167, 44.571857099999995],
                zoom: 7
            });

            myPlacemark = new ymaps.Placemark([/*55.76, 37.64*/  40.2000167, 44.571857099999995], {
                hintContent: 'Yerevan!', balloonContent: 'Capital of Armenia'
            });

            myMap.geoObjects.add(myPlacemark);
        }


        var map = null;
        function showlocation() {
            navigator.geolocation.getCurrentPosition(callback);
        }

        function callback(position) {

            var lat = position.coords.latitude;
            var lon = position.coords.longitude;

            document.getElementById('latitude').innerHTML = lat;
            document.getElementById('longitude').innerHTML = lon;

            var latLong = new google.maps.LatLng(lat, lon);

            var marker = new google.maps.Marker({
                position: latLong
            });

            marker.setMap(map);
            map.setZoom(8);
            map.setCenter(marker.getPosition());
        }

        google.maps.event.addDomListener(window, 'load', initMap);
        function initMap() {
            var mapOptions = {
                center: new google.maps.LatLng(0, 0),
                zoom: 1,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            map = new google.maps.Map(document.getElementById("map-canvas"),
                    mapOptions);

        }


    </script>

</head>

<body>
<div id="map" style="width: 600px; height: 400px"></div>

<br/><br/><br/>
<div>
    <input type="button" value="Show my coordinates" onclick="javascript:showlocation()"/> <br/>

    Latitude: <span id="latitude"></span><br/>
    Longitude: <span id="longitude"></span>
    <br/><br/>
    <div id="map-canvas"/>
</div>
</body>

</html>

<body>

<br>
<span class="well pull-left">
		    <a href="<c:url value='/welcome'/>"><spring:message code="button.yourPage.label"/></a>
		</span>

</body>
