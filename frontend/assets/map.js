var markers = [];

function initMarker (lat, lng, id, type) {
    markers.push(new kakao.maps.Marker({
        map: map,
        title: String(id),
        position: new kakao.maps.LatLng(lat, lng),
        image: new kakao.maps.MarkerImage(
            'markers/tile00' + type + '.png',
            new kakao.maps.Size(24, 36),
            {offset: new kakao.maps.Point(12, 0)}
        )
    }));
    kakao.maps.event.addListener(markers[markers.length-1], 'click', function() {
        onClickMarker.postMessage(id);
    })
}

function setMarker (idx) {
    markers[idx].setMap(map);
}
function unsetMarker (idx) {
    markers[idx].setMap(null);
}

var currentPosition;
var currentImage = new kakao.maps.MarkerImage(
    'markers/current.png',
    new kakao.maps.Size(24, 24),
    {offset: new kakao.maps.Point(12, 12)}
    );
    

function panToCurrent(lat, lng) {
    if(currentPosition) {
        currentPosition.setMap(null);
    }
    var a = 1;
    currentPosition = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(lat, lng),
        image: currentImage
    });
    map.panTo(new kakao.maps.LatLng(lat, lng));
}

var pinDropped = new kakao.maps.Marker(
    {
        position: map.getCenter()
    }
);

function dropPin(mouseEvent) {
    pinDropped.setPosition(mouseEvent.latLng);
    custom.setPosition(mouseEvent.latLng);
}

function openSuggestion() {
    onClickSuggestion.postMessage('test')
}

function openReport() {
    onClickReport.postMessage('test')
}

kakao.maps.event.addListener(pinDropped, 'click', function() {
    var position = pinDropped.getPosition();
    //onClickMarker.postMessage('lat: ' + position.getLat().toPrecision(9) + ', lng: ' + position.getLng().toPrecision(9));

    custom.setMap(custom.getMap() ? null : map);
});

kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
    onClickMap.postMessage('click');
});

custom = new kakao.maps.CustomOverlay({
    clickable: true,
    content: '<div class="customoverlay">' +
    '  <button class="title" onclick="openSuggestion()">건의</button>' +
    '  <button class="title" onclick="openReport()">제보</button>' +
    '</div>'
});

map.setMaxLevel(6);