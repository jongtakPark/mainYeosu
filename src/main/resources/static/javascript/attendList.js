
$(document).ready(function(){
    var target = $(".mainlist:first-child .listb");

    $(document).clearQueue().on("click", ".mainlist:first-child .listc", function(e){
        target.slideDown();
        target.addClass('ad');
        if(target.hasClass('ad')){
            target.stop().slideDown();
            // target.stop().slideUp();
        }
    });

    $(document).mouseup(function(e){
        if(target.has(e.target).length==0){
            target.slideUp();
            target.removeClass('ad');
        }
    })
})

$(document).ready(function(){
    var target = $(".mainlist:nth-child(2) .listb");

    $(document).on("click", ".mainlist:nth-child(2) .listc", function(e){
        target.slideDown();
        target.addClass('ad');
        if(target.hasClass('ad')){
            target.stop().slideDown();
            // target.stop().slideUp();
        }
    });

    $(document).mouseup(function(e){
        if(target.has(e.target).length==0){
            target.slideUp();
            target.removeClass('ad');
        }
    })
})

$(document).ready(function(){
    var target = $(".mainlist:nth-child(3) .listb");

    $(document).on("click", ".mainlist:nth-child(3) .listc", function(e){
        target.slideDown();
        target.addClass('ad');
        if(target.hasClass('ad')){
            target.stop().slideDown();
            // target.stop().slideUp();
        }
    });

    $(document).mouseup(function(e){
        if(target.has(e.target).length==0){
            target.slideUp();
            target.removeClass('ad');
        }
    })
})