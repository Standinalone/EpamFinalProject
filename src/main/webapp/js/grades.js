document.addEventListener('DOMContentLoaded', listen);
		
		
function listen(){
	const form = document.getElementById('form');
	form.addEventListener('submit', hideInputs);
	document.querySelectorAll('.grade').forEach(item => {
	  item.addEventListener('change', event => func(event))
	})
}

function func(event){
	console.log(event.target.value)
	event.target.classList.add("changed")
}

function hideInputs(event){
	
	document.querySelectorAll('.grade:not(.changed)').forEach(item => {
	  item.disabled = true;
	})
	
	//event.preventDefault();
}