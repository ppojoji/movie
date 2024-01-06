$(document).ready(()=>{
	/**
	 * 새로운 출연 정보를 나타냄
	 */
	const castingReg = {
		movieSeq: undefined,
		actorSeq: undefined,
		castingName: undefined,
		role: undefined
	}
	$(".searchbox").on('click', '.item', (e)=>{
		const $movie = $(e.target)
		console.log($movie.data("movie"), $movie.data('title'));
		castingReg.movieSeq = $movie.data("movie")
		
	})
	$("#casting").on('blur',(e)=>{
		//if(e.key === 'Enter'){
			castingReg.castingName = $(e.target).val();
			console.log(castingReg);
		//}
	})
	$('input[name="role"]').on('change',(e)=>{
		castingReg.role = $(e.target).val();
		console.log(castingReg);
	});
	$("#newCasting").on('click',(e)=>{
		castingReg.actorSeq = $('#actorSeq').val()
		$.ajax({
			type:"POST",
			url:`/admin/movie/${castingReg.movieSeq}/actor/${castingReg.actorSeq}`,
			data: castingReg,
			success(res){
				console.log(res);
				document.location.reload();
			},
		});
	})
	
	$("#movie").on('keyup',(e)=>{
		if(e.key === 'Enter'){
			console.log(e.target.value);
			let keyword = e.target.value;
			$.ajax({
			type:"GET",
			url:`/admin/movie/keyword`,
			data: {keyword},
			success(movies){
				console.log(movies);
				// $(".searchbox").text(keyword);
				const $search = $('.searchbox').show()
				movies.forEach(movie => {
					const html = `<div class='item' data-title="${movie.movie_title}" data-movie="${movie.movie_seq}">${movie.movie_title}</div>`;
					$search.append(html)
					
				})
				if(movies.length === 0){
					$search.append(`<div>
						<h5>결과없음</h5>
						<button class="btn-regform">작품 등록</button>
					</div>`)
				}
				//alert('수정 되었습니다.')
				
			},
		});
		}
	})
	$('.searchbox').on('click', '.btn-regform', (e)=>{
		console.log("버튼 눌륌")
		$("#movie-reg-form").show();
	});
	
	$('#movie-reg-form')
		.on('click', '.btn-movie-reg', (e)=> {
			console.log("등록");
			const title= $("#name").val();
			$.ajax({
				url:"/admin/movie/movieReg",
				method:"POST",
				data:{title},
				success(res){
					console.log(res) // res, res.movie (seq: 3233)
				}
				
			})
		})
		.on('click', '.btn-modal-close', (e) => {
			$('#movie-reg-form').hide()
		})
	
	$('#editActor').on('click',(e)=>{
		console.log("수정");
		
		const actorName = $('#actorName').val();
		// const casting = $('#casting').val();
		const birth = $('#birth').val();
		const sex = $('input[name="sex"]:checked').val();
		// const role = $('input[name="role"]:checked').val();
		// const pic = $("#pic")[0];
		const actorSeq = $("#actorSeq").val();
		
		const form = new FormData()
		form.append('actorSeq',actorSeq)
		form.append('actorName', actorName);
		form.append('birth',birth);
		form.append('sex',sex);
		
		// 배우를 수정
		$.ajax({
			type:"POST",
			url:`/admin/edit/actor`,
			data:form,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success(res){
				console.log(res.actor_name);
				alert('수정 되었습니다.')
				
			},
		});
	})
	$('#pic-form').on('change',(e)=>{
		console.log(e.target.files[0]);
		const actorSeq = $('#actorSeq').val()
		const actorPic = $('#pic').val()
		const form = new FormData();
		form.append("file",e.target.files[0]);
		$.ajax({
			method : 'POST',
			url:`/admin/actor/${actorSeq}/pic`,
			data: form,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success(res){
				console.log(res.actor_name);
				$('.actor-pic > img').attr('src', `/detailImg/${res.actor_pic}`)
				
			},
		})
	})
	
	$(".actor-edit").on("click",(e)=>{
		$("#casting-edit-form").show();
		// console.log(e);
		let movieSeq = $(e.target).data('movie')
		let actorSeq = $("#actorSeq").val();
		// console.log("actorSeq >>> " + actorSeq)
		// console.log("movieSeq >>> " + movieSeq);
		$.ajax({
			method : "GET",
			url:`/admin/casting/actor/${actorSeq}/movie/${movieSeq}`,
			success(res){
				console.log(res);
				$(".castingName").val(res.castingName);
				//$('input[name="role"]:checked').val();
				$(`input[type="radio"][value="${res.castingRole}"]`).prop('checked', true)
				$('.casting-form .movieSeq').val(res.movieSeq)
			}
		})
	})
	$('.btn-close').on("click",(e)=>{
		$(".modal").css("display","none");
	})
	
	$(".btn-reg").on("click",(e)=>{
		
		let movieSeq =$('.casting-form .movieSeq').val()
		let actorSeq = $("#actorSeq").val();
		let role = $('input[name="role"]:checked').val();
		let castingName = $(".castingName").val();
		
		$.ajax({
			method : "POST",
			url:`/admin/casting/actorUd`,
			data:{actorSeq,movieSeq,role,castingName},
			success(res){
				console.log(res);
				document.location.reload();
				// $('actor-edit').hide();
			}
		})
	})
});