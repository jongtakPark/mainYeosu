document.addEventListener('DOMContentLoaded', function() {
	var startDay;
   	var endDay;
	console.log(startDay);
	

    

    
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {


			select: function(start){
            var header = $("meta[name='_csrf_header']").attr('content');
			var token = $("meta[name='_csrf']").attr('content');
      		
      		$.ajax({
      			url: "/lease/reservation",
      			type: 'POST',
      			async:false,
 				data: JSON.stringify(start),
				contentType: "application/json",
 				dataType: 'json',
 				beforeSend: function(xhr){
        xhr.setRequestHeader(header, token);
    	},
    	success : function(result){
    			
				console.log(result);
				startDay = result.startDay;
				console.log(startDay);
				endDay = result.endDay;
			},
			error : function(){
				
			}
       		});
      },
		
		
      height: '800px',
      expandRows: true,
      slotMinTime: '08:00',
      slotMaxTime: '20:00',
      headerToolbar: {
        center: 'title',
      },
      locale: "ko",
      initialDate: '2026-07-17',
      initialView: 'dayGridMonth',
      editable: true,
      selectable: true,
      nowIndicator: true,
      dayMaxEvents: true, 
      
      events: [
        {
          title: 'All Day Event',
          start: '2023-01-01',
        },
        {
          title: 'Long Event',
          start: startDay,
          end: endDay
        },
        {
          groupId: 999,
          title: 'Repeating Event',
          start: '2023-01-09T16:00:00'
        },
        {
          groupId: 999,
          title: 'Repeating Event',
          start: '2026-07-16T16:00:00'
        },
        {
          title: 'Conference',
          start: '2023-01-11',
          end: '2023-01-13'
        },
        {
          title: 'Meeting',
          start: '2023-01-12T10:30:00',
          end: '2023-01-12T12:30:00'
        },
        {
          title: 'Lunch',
          start: '2023-01-12T12:00:00'
        },
        {
          title: 'Meeting',
          start: '2023-01-12T14:30:00'
        },
        {
          title: 'Happy Hour',
          start: '2023-01-12T17:30:00'
        },
        {
          title: 'Dinner',
          start: '2023-01-12T20:00:00'
        },
        {
          title: 'Birthday Party',
          start: '2023-01-13T07:00:00'
        },
        {
          title: 'Click for Google',
          url: 'http://google.com/',
          start: '2023-01-28'
        }
      ],
      
		
		
    });


    calendar.render();
  });

