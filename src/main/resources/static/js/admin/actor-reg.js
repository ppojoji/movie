$(document).ready(()=>{
	/*const checkForm = () => {
		return false // 양식에 문제가 없음
		return true // 폼에 문제가 있을대
	}*/
		
	let movieSeq = undefined // 
	$('.searchbox').on('click',(e)=>{
		const $movie = $(e.target);
		movieSeq = $movie.data('movie')
		$('#movie').val($movie.text());
	})
	
	const registerActor = (isDirector) => {
		const url = isDirector ? "/admin/director2" : "/admin/actor"
		
		const actorName = $('#actorName').val();
		const casting = $('#casting').val();
		const birth = $('#birth').val();
		const sex = $('input[name="sex"]:checked').val();
		const role = $('input[name="role"]:checked').val();
		const pic = $("#pic")[0];
		
		const form = new FormData()
		form.append('actorName', actorName);
		form.append('casting',casting);
		form.append('birth',birth);
		form.append('sex',sex);
		form.append('role',role);
		form.append('pic',pic.files[0]);
		
		if (movieSeq) {			
			form.append('movieSeq',movieSeq);
		}
		
		console.log("ddd " + sex)
		/**
		 * 배우/감독 공통 검증
		 */
		if(!actorName){
			alert('배우 이름을 입력해 주세요.');
			return;
		}else if(!birth){
			alert('생년월일을 입력해 주세요.');
			return;
		}else if(!sex){
			alert('성별을 입력해 주세요.');
			return;
		}
		/**
		 * 배우인 경우는 배역과 역할이 있어야 함
		 */
		if (!isDirector) {
			if(!casting){
				alert('배역을 입력해 주세요.');
				return;
			}
			else if(!role){
				alert('역할을 입력해 주세요.');
				return;
			}	
		}
				
		// 배우를 신규 등록
		$.ajax({
			type:"POST",
			url,
			data:form,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success(res){
				console.log(res.actor_name);
				alert('등록 되었습니다.')
				
			},
		});
	}
	
	$('#addActor').on('click',(e)=>{
		console.log(e);
		registerActor(false)
	})
	
	$('#addDirector').on('click',(e)=>{
		registerActor(true);
	})
		
	const searchDirectory = (keyword) => {
		$.ajax({
			type: 'GET',
			url: '/admin/search/movie',
			data: {keyword: keyword},
			success: (movie) => {
				console.log(movie)
				
				$(".searchbox").css('display', 'block');
				movie.forEach((e)=>{
					$(".searchbox").append(
						`<div class="item" data-movie="${e.movie_seq}">${e.movie_title}</div>`
					)	
				})
				if(movie.length === 0){
					$(".searchbox").append("없음");
				}
			}
		})
	}
	
	$("#movie").on("keyup",(e)=>{
		 console.log(e.key)
		 if(e.key === 'Enter') {
		 	let movie = $("#movie").val();
		 	searchDirectory(movie)
		 }	
	 })
});