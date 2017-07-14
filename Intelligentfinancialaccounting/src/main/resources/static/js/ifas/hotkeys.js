$(document).on('keydown', null, 'c', launchAddCustomerModal)
			.on('keydown', null, 'shift+c', redirectToCustomerIndex)
			.on('keydown', null, 'i', launchAddItemModal)
			.on('keydown', null, 'shift+i', redirectToItemIndex)
			.on('keydown', null, 'b', launchAddBankModal)
			.on('keydown', null, 'shift+s', redirectToSales)
			.on('keydown', null, 's', redirectToAddMultiSales)
			.on('keydown', null, 'shift+p', redirectToPurchase)
			.on('keydown', null, 'p', redirectToAddMultiPurchase)
			.on('keydown', null, 'shift+f', redirectToFault)
			.on('keydown', null, 'f', redirectToAddMultiFault)
			.on('keydown', null, 't', redirectToAddTransaction)
			.on('keydown', null, 'x', redirectToIndex)
			.on('keydown', null, 'l', logoutApplication);

function launchAddCustomerModal(){
	$('.modal').modal('hide');
	$('#addCustomerModal').modal('show');
}

function launchAddItemModal(){
	$('.modal').modal('hide');
	$('#addItemModal').modal('show');
}

function launchAddBankModal(){
	$('.modal').modal('hide');
	$('#addBankModal').modal('show');
}

function redirectToCustomerIndex(){
	$(location).attr('href', '/listCustomers?pageno=1&sorttype=normal');
}

function redirectToItemIndex(){
	$(location).attr('href', '/listItems?pageno=1&sorttype=normal');
}

function redirectToSales(){
	$(location).attr('href', '/showSalesSheet');
}

function redirectToAddMultiSales(){
	$(location).attr('href', '/addMultiSales');
}

function redirectToPurchase(){
	$(location).attr('href', '/showPurchaseSheet');
}

function redirectToAddMultiPurchase(){
	$(location).attr('href', '/addMultiPurchase');
}

function redirectToFault(){
	$(location).attr('href', '/showFaultSheet');
}

function redirectToAddMultiFault(){
	$(location).attr('href', '/addMultiFault');
}

function redirectToAddTransaction(){
	$(location).attr('href', '/addTransaction');
}

function redirectToIndex(){
	$(location).attr('href', '/home');
}

function logoutApplication(){
	$(location).attr('href', '/logout');
}
