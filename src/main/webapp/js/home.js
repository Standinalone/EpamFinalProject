document.addEventListener('DOMContentLoaded', listen);

function listen() {
	var table = document.getElementById("mainTable");
	if (table != null) {
		var row = table.rows[0]
		for (var j = 0; j < row.cells.length; j++)
			row.cells[j].onclick = function() {
				tableText(this);
			}
	}
	var selects = document.getElementsByTagName('select')
	for (var i = 0; i < selects.length; i++){
		selects[i].onchange = (event) => changeLocation(event)
	}
}

function changeLocation(event){
	var url = new URL(location)
	console.log(event.target.id)
	console.log(event.target.name)
	console.log( url.toString())
	url.searchParams.set(event.target.name, event.target.value)
	url.searchParams.set('pagenum', 1)
	location = url.toString()
}

function tableText(columnName) {
	var url = new URL(location)
	url.searchParams.set('sort', columnName.id)
	var order = url.searchParams.get('order')
	if (order != null && order === "desc"){
		order = "asc"
	}
	else{
		order = "desc"
	}
	url.searchParams.set('order', order)
	url.searchParams.set('pagenum', 1)
    location = url.toString()
}