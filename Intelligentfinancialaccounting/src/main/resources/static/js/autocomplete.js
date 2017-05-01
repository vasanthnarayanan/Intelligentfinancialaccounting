function autocomplete(searchBoxId,suggestionDivId,tableName,fieldName)
{
	/*-- Global variable--*/
	var selectedItem = null;
	var $autocomplete=$(suggestionDivId);
	var $searchBox=$(searchBoxId);
	

		/*-- highlight selection and store values --*/
		var setSelectedItem = function(item) {
		selectedItem = item;
		if (selectedItem === null) {
					$autocomplete.hide();
					return;
					}
		if (selectedItem < 0) {
					selectedItem = 0;
					}
		if (selectedItem >= $autocomplete.find('p').length) {
					selectedItem = $autocomplete.find('p').length - 1;
					}
					};
			
		/*-- Highlight the field --*/
	var highlightField = function(){
		$autocomplete.find('p').removeClass('selected')
		.eq(selectedItem).addClass('selected');
		};
		
		/*-- Populate Search field on enter key pressed --*/
		var populateSearchField = function() {
			$searchBox.val($autocomplete
			.find('.selected').text());
			};
			
	/*--Start of keyup event--*/
	$searchBox.attr('autocomplete', 'off').keyup(function(event)
		{
		// Initializing height width and position
		var top=$searchBox.offset().top + $searchBox.outerHeight(true);
		var left=$searchBox.offset().left;
		var width=$searchBox.innerWidth(true);
		
		var suggestioncssobj={
							"top":top,
							"left":left,
							"width":width
							};
		
		$autocomplete.css(suggestioncssobj);
	// attr('autocomplete', 'off') is used to disable inbuilt autocomplete features of browsers
	if (event.keyCode > 40 || event.keyCode == 8) {
		// Keys with codes 40 and below are special
		// (enter, arrow keys, escape, etc.).
		// Key code 8 is backspace.
		// dumping data's frm suggestion box before ajax request
	$.ajax({
		   type: "POST",
		   url: "../ajaxservice.do",
		   data: {enteredcharacter:$searchBox.val(),action: "indexsearchajax",tablename: tableName,fieldname: fieldName},
		   dataType: 'json',
		   success: function(responseData){
			   /*--Show or Hide box--*/
				if($searchBox.val()=='')
					{
					$autocomplete.hide();	
					}
				else
					{
				$autocomplete.show();
					}
				/*--End Show or Hide box--*/
				$autocomplete.empty();
				   $.each(responseData,function(index,value){
				   var _msg=$.trim(value.fieldName);
				   if(responseData.error!=="yes")
					   {
						// appending suggestions					
					   	$("<p><a>"+_msg+"</a></p>").appendTo($autocomplete)
					   								.mouseover(function() { /*-- On mouse over to navigate frm current hover position --*/
					   														highlightField();
					   														setSelectedItem(index);
					   													   })
					   								.click(function() {  /*-- On click event for suggestion drop selection --*/
					   														$searchBox.val(_msg);
					   														$autocomplete.hide();
					   												  });
					  // disable selection of text
					  $autocomplete.bind('selectstart',function(){
						  return false;
					  }) ;	
						// initializing default value
					   setSelectedItem(0);
					  }
					   else {
					   setSelectedItem(null);
					   }
				   /*-- End of if loop --*/
			   });
			   /*-- End of each loop --*/
		   	}
		   /*-- End of success function --*/
			});
	/*-- End of ajax --*/
			}
		else if (event.keyCode == 38 && selectedItem !== null) {
		// User pressed up arrow.
		highlightField();
		populateSearchField();
		setSelectedItem(selectedItem - 1);
		event.preventDefault();
		}
		else if (event.keyCode == 40 && selectedItem !== null) {
		// User pressed down arrow.
		highlightField();
		populateSearchField();
		setSelectedItem(selectedItem + 1);
		event.preventDefault();
		}
  /*-- End of keyboard track if loop --*/			 
		   }).keypress(function(event) {
				if (event.keyCode == 13 && selectedItem !== null) {
					// User pressed enter key.
					populateSearchField();
					$autocomplete.hide();
					event.preventDefault();
					}
		   }).blur(function(event) {
			   setTimeout(function() {
				   setSelectedItem(null);
				   }, 250);
		   });
	
/*--End of keyboard(Keyup,enter and blur(when text field loses focus)) event--*/
	
}

function devbridgeAutoComplete(searchBoxId,action){
	$(searchBoxId).devbridgeAutocomplete({
    	serviceUrl:action,
    	minChars:1,
    	noCache:true,
    	onSearchStart: function(query){
    		autocompleteOnSearch(searchBoxId,query);
    	},
    	onSelect: function (suggestion){
    		autocompleteOnSelect(searchBoxId,suggestion);
    	}
    });
}