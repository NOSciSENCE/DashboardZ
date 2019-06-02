function addLoadEvent(func){ 
    var oldonload=window.onload; 
    if(typeof window.onload!='function'){ 
        window.onload=func; 
    }else{ 
        window.onload=function(){ 
            oldonload(); 
            func(); 
        } 
    } 
} 
function adminCheck(){
	$.ajax({
		url:'../admin/check',
		type:'GET',
		dataType:'json',
		data:{},
		success:function(data){
			if(data.code == 100){
				$("#cap").hide();
				$("#adminName").text(data.extend.admin.name);
			}
			else{
				$("#commandcap").hide();
			}
		},
		error:function(){
			$("#commandcap").hide();
		}
	})
}



function logout(){
	$.ajax({
		url:'../admin/logout',
		type:'POST',
		dataType:'json',
		data:{},
		success:function(){
			window.location.href="../login.html"; 
		},
		error:function(){
			
		}
		
	})
	
}

addLoadEvent(adminCheck)