$(document).ready(function() {
	  $('#sel').change(function() {
	    var result = $('#sel option:selected').val();
	    if (result == 'volunteer') {
	      $('#cont3').show();
	      $('#cont2').hide();
	    } else {
	      $('#cont3').hide();
	      $('#cont2').show();
	    }
	  }); 
}); 