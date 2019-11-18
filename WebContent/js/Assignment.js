/** Assignment.js was created by Mark Renard on 11/17/2019.
 *
 * This script provides functionality to otherwise hidden assignment buttons
 * as defined in list.jsp.
 * 
 * When the user selects an assignment category from a drop-down menu, the
 * buttons next to the problems not in that category are displayed. Check
 * marks appear near problems already in that category.
 */

$(document).ready(function(){
	
	// Prepares form elements when the user selects an assignment category
	$('select[name="assignment-category"]').change(function(){
		var cid = $(this).val();
		
		// Sets category id in hidden form element
		$("#assignment-cid").val(cid);
		
		prepareAssignmentButtons(cid);
	})
		
	// Shows each assignment button not in the selected category, hides others
	function prepareAssignmentButtons(cid){
		hideIffInCategory(".assign-button", cid);
		hideIffInCategory(".assign-glyphicon", cid);
		showIffInCategory(".added-glyphicon", cid);
	}
	
	// Hides elements of class elementClass with identical cid, shows others
	function hideIffInCategory(elementClass, cid, display){
		
		//Default display parameter is "inline"
		var display = (typeof display !== 'undefined') ? display : "inline";
		
		$(elementClass).each(function(){
			if ($(this).attr("cid") != cid){
				$(this).attr("style", "display: " + display);
			} else {
				$(this).attr("style", "display: none");
			}
		})
	}
	
	// Shows elements of class elementClass with identical cid, hides others
	function showIffInCategory(elementClass, cid, display){
		
		//Default display parameter is "inline"
		var display = (typeof display !== 'undefined') ? display : "inline";
		
		$(elementClass).each(function(){
			if ($(this).attr("cid") == cid){
				$(this).attr("style", "display: " + display);
			} else {
				$(this).attr("style", "display: none");
			}
		})
	}
	
	// Sets the problem id when a category assignment button is clicked
	$(".assign-button").click(function(){
		var pid = $(this).attr("pid");
		
		// Sets problem id in hidden form element
		$("#assignment-pid").val(pid);
		
		$(this).attr("type", "submit");
	})
})