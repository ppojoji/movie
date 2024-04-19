$(document).ready(()=>{
	console.log("dddd")
	const $joinForm = $(".join-form")
	
	$joinForm
	.on('click','.join',(e)=>{
		console.log("eee");
		let id = $(".id").val();
		let email = $(".email").val();
		let pwd = $(".pwd").val(); 
		
		console.log(id, email, pwd);
		
		$.ajax({
			type:"POST",
			url:"/insertJoin",
			data:{id,email,pwd},
			success(res) {
				console.log(res);
				alert("회원가입 되었습니다.");
				location.href = "/";
			}
		})
	})
	.on('blur','.id',(e)=>{
		let id = $(".id").val();
		
		$.ajax({
			type:"POST",
			url:"/join/existing/id",
			data:{id},
			success(existing) {
				console.log(existing);
				if(existing){
					$('.error').text("이미 존재하는 아이디 입니다.");
				}else{
					$('.error').text("");
				}
				$('.join').attr("disabled", existing);
			}
		})
	})
})