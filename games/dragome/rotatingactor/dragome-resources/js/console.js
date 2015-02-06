
var flag=false;$(document).ready(function()
{$(".toggle").click(function()
{$(".panel").slideToggle("slide");$("#content").animate({height:'200px',bottom:'+=350'},1000)
$(this).removeClass("collapsed expanded");if(flag)
$(this).addClass('collapsed');else
$(this).addClass('expanded');flag=!flag;});});