
String.prototype.hashCode=function(){if(Array.prototype.reduce){return this.split("").reduce(function(a,b){a=((a<<5)-a)+b.charCodeAt(0);return a&a},0);}
var hash=0;if(this.length===0)return hash;for(var i=0;i<this.length;i++){var character=this.charCodeAt(i);hash=((hash<<5)-hash)+character;hash=hash&hash;}
return hash;}
if(!String.prototype.replaceAll){String.prototype.replaceAll=function(str1,str2,ignore)
{return this.replace(new RegExp(str1.replace(/([\/\,\!\\\^\$\{\}\[\]\(\)\.\*\+\?\|\<\>\-\&])/g,"\\$&"),(ignore?"gi":"g")),(typeof(str2)=="string")?str2.replace(/\$/g,"$$$$"):str2);};}
if(!String.prototype.repeat){String.prototype.repeat=function(n)
{n=Math.max(n||0,0);return new Array(n+1).join(this.valueOf());};}
if(!String.prototype.startsWith){String.prototype.startsWith=function(searchString,position)
{position=Math.max(position||0,0);return this.indexOf(searchString)==position;};}
if(!String.prototype.endsWith){String.prototype.endsWith=function(searchString,endPosition)
{endPosition=Math.max(endPosition||0,0);var s=String(searchString);var pos=this.lastIndexOf(s);return pos>=0&&pos==this.length-s.length-endPosition;};}
if(!String.prototype.contains){String.prototype.contains=function(searchString,position)
{position=Math.max(position||0,0);return this.indexOf(searchString)!=-1;};}
if(!String.prototype.toArray){String.prototype.toArray=function()
{return this.split('');};}
if(!String.prototype.reverse){String.prototype.reverse=function()
{return this.split('').reverse().join('');};}
String.validBrackets=function(br)
{if(!br){return false;}
var quot="''\"\"`'``";var sgl="<>{}[]()%%||//\\\\";var dbl="/**/<??><%%>(**)";return(br.length==2&&(quot+sgl).indexOf(br)!=-1)||(br.length==4&&dbl.indexOf(br)!=-1);};String.prototype.brace=String.prototype.bracketize=function(br)
{var string=this;if(!String.validBrackets(br)){return string;}
var midPos=br.length/2;return br.substr(0,midPos)+string.toString()+br.substr(midPos);};String.prototype.unbrace=String.prototype.unbracketize=function(br)
{var string=this;if(!br){var len=string.length;for(var i=2;i>=1;i--){br=string.substring(0,i)+string.substring(len-i);if(String.validBrackets(br)){return string.substring(i,len-i);}}}
if(!String.validBrackets(br)){return string;}
var midPos=br.length/2;var i=string.indexOf(br.substr(0,midPos));var j=string.lastIndexOf(br.substr(midPos));if(i==0&&j==string.length-midPos){string=string.substring(i+midPos,j);}
return string;};Number.prototype.radix=function(r,n,c)
{return this.toString(r).padding(-n,c);};Number.prototype.bin=function(n,c)
{return this.radix(0x02,n,c);};Number.prototype.oct=function(n,c)
{return this.radix(0x08,n,c);};Number.prototype.dec=function(n,c)
{return this.radix(0x0A,n,c);};Number.prototype.hexl=function(n,c)
{return this.radix(0x10,n,c);};Number.prototype.hex=function(n,c)
{return this.radix(0x10,n,c).toUpperCase();};Number.prototype.human=function(digits,binary)
{var n=Math.abs(this);var p=this;var s='';var divs=arguments.callee.add(binary);for(var i=divs.length-1;i>=0;i--){if(n>=divs[i].d){p/=divs[i].d;s=divs[i].s;break;}}
return p.toFixed(digits)+s;};Number.prototype.human.add=function(binary,suffix,divisor)
{var name=binary?'div2':'div10';var divs=Number.prototype.human[name]=Number.prototype.human[name]||[];if(arguments.length<3){return divs;}
divs.push({s:suffix,d:Math.abs(divisor)});divs.sort(function(a,b)
{return a.d-b.d;});return divs;};Number.prototype.human.add(true,'K',1<<10);Number.prototype.human.add(true,'M',1<<20);Number.prototype.human.add(true,'G',1<<30);Number.prototype.human.add(true,'T',Math.pow(2,40));Number.prototype.human.add(false,'K',1e3);Number.prototype.human.add(false,'M',1e6);Number.prototype.human.add(false,'G',1e9);Number.prototype.human.add(false,'T',1e12);Number.fromHuman=function(value,binary)
{var m=String(value).match(/^([\-\+]?\d+\.?\d*)([A-Z])?$/);if(!m){return Number.NaN;}
if(!m[2]){return+m[1];}
var divs=Number.prototype.human.add(binary);for(var i=0;i<divs.length;i++){if(divs[i].s==m[2]){return m[1]*divs[i].d;}}
return Number.NaN;};if(!String.prototype.trim){String.prototype.trim=function()
{return this.replace(/(^\s*)|(\s*$)/g,"");};}
if(!String.prototype.trimLeft){String.prototype.trimLeft=function()
{return this.replace(/(^\s*)/,"");};}
if(!String.prototype.trimRight){String.prototype.trimRight=function()
{return this.replace(/(\s*$)/g,"");};}
String.prototype.dup=function()
{var val=this.valueOf();return val+val;};String.prototype.padding=function(n,c)
{var val=this.valueOf();if(Math.abs(n)<=val.length){return val;}
var m=Math.max((Math.abs(n)-this.length)||0,0);var pad=Array(m+1).join(String(c||' ').charAt(0));return(n<0)?pad+val:val+pad;};String.prototype.padLeft=function(n,c)
{return this.padding(-Math.abs(n),c);};String.prototype.alignRight=String.prototype.padLeft;String.prototype.padRight=function(n,c)
{return this.padding(Math.abs(n),c);};String.prototype.format=function()
{var args=arguments;return this.replace(/\{(\d+)\}/g,function($0,$1)
{return args[$1]!==void 0?args[$1]:$0;});};String.prototype.alignLeft=String.prototype.padRight;String.prototype.sprintf=function()
{var args=arguments;var index=0;var x;var ins;var fn;return this.replace(String.prototype.sprintf.re,function()
{if(arguments[0]=="%%"){return"%";}
x=[];for(var i=0;i<arguments.length;i++){x[i]=arguments[i]||'';}
x[3]=x[3].slice(-1)||' ';ins=args[+x[1]?x[1]-1:index++];return String.prototype.sprintf[x[6]](ins,x);});};String.prototype.sprintf.re=/%%|%(?:(\d+)[\$#])?([+-])?('.|0| )?(\d*)(?:\.(\d+))?([bcdfosuxXhH])/g;String.prototype.sprintf.b=function(ins,x)
{return Number(ins).bin(x[2]+x[4],x[3]);};String.prototype.sprintf.c=function(ins,x)
{return String.fromCharCode(ins).padding(x[2]+x[4],x[3]);};String.prototype.sprintf.d=String.prototype.sprintf.u=function(ins,x)
{return Number(ins).dec(x[2]+x[4],x[3]);};String.prototype.sprintf.f=function(ins,x)
{var ins=Number(ins);if(x[5]){ins=ins.toFixed(x[5]);}else if(x[4]){ins=ins.toExponential(x[4]);}else{ins=ins.toExponential();}
x[2]=x[2]=="-"?"+":"-";return ins.padding(x[2]+x[4],x[3]);};String.prototype.sprintf.o=function(ins,x)
{return Number(ins).oct(x[2]+x[4],x[3]);};String.prototype.sprintf.s=function(ins,x)
{return String(ins).padding(x[2]+x[4],x[3]);};String.prototype.sprintf.x=function(ins,x)
{return Number(ins).hexl(x[2]+x[4],x[3]);};String.prototype.sprintf.X=function(ins,x)
{return Number(ins).hex(x[2]+x[4],x[3]);};String.prototype.sprintf.h=function(ins,x)
{var ins=String.prototype.replace.call(ins,/,/g,'');x[2]=x[2]=="-"?"+":"-";return Number(ins).human(x[5],true).padding(x[2]+x[4],x[3]);};String.prototype.sprintf.H=function(ins,x)
{var ins=String.prototype.replace.call(ins,/,/g,'');x[2]=x[2]=="-"?"+":"-";return Number(ins).human(x[5],false).padding(x[2]+x[4],x[3]);};String.prototype.compile=function()
{var args=arguments;var index=0;var x;var ins;var fn;var result=this.replace(/(\\|")/g,'\\$1').replace(String.prototype.sprintf.re,function()
{if(arguments[0]=="%%"){return"%";}
arguments.length=7;x=[];for(var i=0;i<arguments.length;i++){x[i]=arguments[i]||'';}
x[3]=x[3].slice(-1)||' ';ins=x[1]?x[1]-1:index++;return'", String.prototype.sprintf.'+x[6]+'(arguments['+ins+'], ["'+x.join('", "')+'"]), "';});return Function('','return ["'+result+'"].join("")');};String.prototype.parseUrl=function()
{var matches=this.match(arguments.callee.re);if(!matches){return null;}
var result={'scheme':matches[1]||'','subscheme':matches[2]||'','user':matches[3]||'','pass':matches[4]||'','host':matches[5],'port':matches[6]||'','path':matches[7]||'','query':matches[8]||'','fragment':matches[9]||''};return result;};String.prototype.parseUrl.re=/^(?:([a-z]+):(?:([a-z]*):)?\/\/)?(?:([^:@]*)(?::([^:@]*))?@)?((?:[a-z0-9_-]+\.)+[a-z]{2,}|localhost|(?:(?:[01]?\d\d?|2[0-4]\d|25[0-5])\.){3}(?:(?:[01]?\d\d?|2[0-4]\d|25[0-5])))(?::(\d+))?(?:([^:\?\#]+))?(?:\?([^\#]+))?(?:\#([^\s]+))?$/i;String.prototype.camelize=function()
{return this.replace(/([^-]+)|(?:-(.)([^-]+))/mg,function($0,$1,$2,$3)
{return($2||'').toUpperCase()+($3||$1).toLowerCase();});};String.prototype.uncamelize=function()
{return this.replace(/[A-Z]/g,function($0)
{return'-'+$0.toLowerCase();});};