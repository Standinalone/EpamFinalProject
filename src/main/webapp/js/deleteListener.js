document.addEventListener('DOMContentLoaded', listen);

function listen(){
	document.querySelectorAll('.btn-delete').forEach(item => {
	  item.addEventListener('click', event => checkDeletion(event))
	})
}

function checkDeletion(event){
	if (event.target.tagName !== "svg")
		return
	console.log(event.target)
	console.log(event.target.parentNode.parentNode)
	let result = confirm("Delete?");
	if (result) {
	    console.log('ok')
	  	event.target.parentNode.parentNode.submit()
	}
	
}