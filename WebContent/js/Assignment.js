$(document).ready(function(){

	$('select[name="assignment-category"]').change(function(){
		var cid = $(this).val();
		showAssignmentButtonsNotInCategory(cid);
	})
	
	function showAssignmentButtonsNotInCategory(cid){
		showClassIfNotInCategory(".assign-button", cid);
		showClassIfNotInCategory(".assign-glyphicon", cid);
		
//		$(".assign-button").attr("style", "display: inline");
//		$(".assign-glyphicon").attr("style", "display: inline");
		

		window.alert(cid);
	}
	
	function showClassIfNotInCategory(cls, cid){
		$(cls).each(function(){
			if ($(this).attr("cid") != cid){
				$(this).attr("style", "display: inline");
			} else {
				$(this).attr("style", "display: none");
			}
		})
	}
})