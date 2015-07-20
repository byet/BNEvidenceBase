$(document).ready(function(){

	$(".menu1").click(function()
	{
		$('#menublock1').toggle();
	});
	$(".menu2").click(function()
	{
		$('#menublock2').toggle();
	});
	$(".menu3").click(function()
	{
		$('#menublock3').toggle();
	});
	$(".menu4").click(function()
	{
		$('#menublock4').toggle();
	});
	$(".menu5").click(function()
	{
		$('#menublock5').toggle();
	});
		$(".menu6").click(function()
	{
		$('#menublock6').toggle();
	});
/*	var par = $('.refblock');
	$(par).hide();
	$(".ref").click(function()
	{
		$('.refblock').toggle();
	}); */
	
/*	var par = $('.evrefblock');
	$(par).hide();
	$(".evref").click(function()
	{
		$('.evrefblock').toggle();
	});*/
	
/*	var par = $('.exrefblock');
	$(par).hide();
	$(".exref").click(function()
	{
		$('.exrefblock').toggle();
	}); */
	

	
/*	var par = $('.eviblock');
	$(par).hide();
	$(".evi").click(function()
	{
		$('.eviblock').toggle();
	}); */
	
/*	var par = $('.excblock');
	$(par).hide();
	$(".exc").click(function()
	{
		$('.excblock').toggle();
	}); */
	

	$(".excblock").hide();
	$(".exc").click(function() {
	//		$(".excblock").hide();
			$(this).parent().children(".excblock").toggle(); });

	$(".exrefblock").hide();
	$(".exref").click(function() {
	//		$(".eviblock").hide();
			$(this).parent().parent().children(".exrefblock").toggle(); });	
			
	$(".refblock").hide();
	$(".ref").click(function() {
	//		$(".eviblock").hide();
			$(this).parent().parent().children(".refblock").toggle(); });		
	
	$(".evrefblock").hide();
	$(".evref").click(function() {
	//		$(".eviblock").hide();
			$(this).parent().parent().children(".evrefblock").toggle(); });	
	
	$(".eviblock").hide();
	$(".evi").click(function() {
	//		$(".eviblock").hide();
			$(this).parent().children(".eviblock").toggle(); });	
	  

	
	$("#container").fadeIn(500);
	$("tr").fadeIn(1000);
	$("td").fadeIn(1000);
	$("iframe").fadeIn(2000);
});
