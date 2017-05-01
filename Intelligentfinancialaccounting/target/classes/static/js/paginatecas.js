/*-- Global variables --*/
window.$midup=0;
window.$middown=0;
window.$slidelength=0;
window.noofpagelinks=0;

function createslide(start)
	{
		// checking whether maxlimit value is not exceeded(for goto)
		if((start+$slidelength)>noofpagelinks)
			{
			start=start-1;
			}
		
		$('.pages').empty();
		for(var i=start;i<(start+$slidelength);i++)
		{
		$('<li><a href="/customerAccountSheet?customername='+customerName+'&customerid='+customerId+'&pageno='+(i+1)+'">'+(i+1)+'</a></li>').appendTo('.pages');
		}
	}

//to keep track of selected items
function setSelectedItem(item) {
	window.selectedItem = item;
	if (selectedItem < 0) {
				selectedItem = 0;
				}
	if (selectedItem >= noofpagelinks) {
				selectedItem = noofpagelinks-1;
				}
	if(selectedItem<$midup)
		{
	createslide(0);
	highlightSelection(selectedItem); // to highlight page in first slide
		}
	else if(selectedItem>(noofpagelinks-$midup))
		{
	createslide(selectedItem-$midup);    // to middle selection to index 2	
	highlightSelection($slidelength-(noofpagelinks-selectedItem)); // to highlight page in last slide
		}
	else
		{
	createslide(selectedItem-$middown);    // to middle selection to index 2
	highlightSelection($middown);	
		}
	$('.totalpages').html((selectedItem+1)+"&nbsp;of&nbsp;"+noofpagelinks+"&nbsp;pages"); // display pages
	
}

//to highlight selected item
function highlightSelection(page)
{
	if(noofpagelinks==1)
	{
	$('#pagination-ifas > li.last').removeClass('last').addClass('last-off');	
	$('#pagination-ifas > li.first').removeClass('first').addClass('first-off');
	$('#pagination-ifas > li.next').removeClass('next').addClass('next-off');
	$('#pagination-ifas > li.previous').removeClass('previous').addClass('previous-off');	
	}
	else if(selectedItem==0 && noofpagelinks>1)
	{
	$('#pagination-ifas > li.last-off').removeClass('last-off').addClass('last');
	$('#pagination-ifas > li.first').removeClass('first').addClass('first-off');
	$('#pagination-ifas > li.next-off').removeClass('next-off').addClass('next');
	$('#pagination-ifas > li.previous').removeClass('previous').addClass('previous-off');
	}
	else if(selectedItem==(noofpagelinks-1))
	{
	$('#pagination-ifas > li.first-off').removeClass('first-off').addClass('first');
	$('#pagination-ifas > li.last').removeClass('last').addClass('last-off');	
	$('#pagination-ifas > li.previous-off').removeClass('previous-off').addClass('previous');
	$('#pagination-ifas > li.next').removeClass('next').addClass('next-off');
	}
	else
	{
	$('#pagination-ifas > li.last-off').removeClass('last-off').addClass('last');
	$('#pagination-ifas > li.first-off').removeClass('first-off').addClass('first');
	$('#pagination-ifas > li.next-off').removeClass('next-off').addClass('next');
	$('#pagination-ifas > li.previous-off').removeClass('previous-off').addClass('previous');
	}
	$paginationdiv.find('li a').removeClass('active').eq(page).addClass('active');
}

//for previous
$('#pagination-ifas .previous').live("click",function(event){
	setSelectedItem(selectedItem-1);
	
	window.location="/customerAccountSheet?customername="+customerName+"&customerid="+customerId+"&pageno="+(pageNo-1)+"";
});

// for next
$('#pagination-ifas .next').live("click",function(event){
	setSelectedItem(selectedItem+1);
	
	window.location="/customerAccountSheet?customername="+customerName+"&customerid="+customerId+"&pageno="+(pageNo+1)+"";
});

// for first
$('#pagination-ifas .first').live("click",function(event){
	createslide(0);
	setSelectedItem(0);
	window.location="/customerAccountSheet?customername="+customerName+"&customerid="+customerId+"&pageno=1";
});

// for last
$('#pagination-ifas .last').live("click",function(event){
	createslide(noofpagelinks-$slidelength);
	setSelectedItem(noofpagelinks-1);
	window.location="/customerAccountSheet?customername="+customerName+"&customerid="+customerId+"&pageno="+noofpagelinks+"";
});

//for goto
$("#go").live("click",function(){
	$gotoval = $("#gotopage").val();
	if ($gotoval < 1) {
		$gotoval = 1;
		}
	if ($gotoval >= noofpagelinks) {
		$gotoval = noofpagelinks;
		}
	window.location="/customerAccountSheet?customername="+customerName+"&customerid="+customerId+"&pageno="+$gotoval+"";
});

function pagination(totalrows,recperpage,tName,cName,pNo){
	window.tableName=tName;
	window.customerName=cName;
	window.pageNo=pNo;
	window.$paginationdiv=$('#pagination-ifas');
	var $totalnoofrows=totalrows;
	window.$recperpage=recperpage;
	noofpagelinks=Math.ceil($totalnoofrows/$recperpage);
	
	if(noofpagelinks>4)
	{
			$slidelength=5;
	}
	else
	{
			$slidelength=noofpagelinks;
	}
	createslide(0);// default
	
	
	if($slidelength<3)
		{
		$midup=1;
		$middown=1;
		}
	else if($slidelength%2==0)
		{
		$midup=($slidelength/2);
		$middown=$midup;
		}
	else if($slidelength%2!=0)
		{
		$midup=Math.ceil($slidelength/2);
		$middown=$midup-1;
		}	
}