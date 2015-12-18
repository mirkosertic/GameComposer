
function WebSocketInitializer()
{if(getQuerystring("debug")=="true")
{var socket=jQuery.atmosphere;var request={timeout:600000000,url:'dragome-websocket',contentType:"application/json",transport:'websocket',binaryType:'arraybuffer'};window.onbeforeunload=function()
{socket.unsubscribe();};request.onOpen=function(response)
{_ed.runApplication();};request.onMessage=function(response)
{var message=response.responseBody;{}
if(window.onSocketMessage)
window.onSocketMessage(message);};request.onClose=function(response)
{};request.onError=function(response)
{};window.subSocket=socket.subscribe(request);}};$(document).ready(function()
{WebSocketInitializer();});