document.addEventListener('DOMContentLoaded', listen);
		
var idempotencyId = null

function listen(){
	var d = new Date()
	var randomPart = Math.random().toString(36).substring(7)
	var dateTime = `${d.getFullYear()}-${d.getMonth()}-${d.getDay()}/${d.getHours()}:${d.getMinutes()}:${d.getSeconds()}:${d.getMilliseconds()}`
	var uid = dateTime + randomPart
	document.cookie = `uid=${uid}`
	console.log(uid)
}