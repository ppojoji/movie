$(document).ready(()=>{
	let directorSeq = undefined
	/**
	 * 현재 영화의 출연진 목록(감독, 배우)
	 */
	let actors = []
	let editingActor = undefined // 트리니티
	let editingMovie = undefined
	
	// let directorSeqs = []
	$(".searchbox").on('click',(e)=>{
		directorSeq = $(e.target).data('actor')
		$("#director").val($(e.target).text());
		console.log(directorSeq)
		$(".searchbox").empty().css("display",'none');
		
	})
	
	//$("#addMovie").on()
	const getMovieSeq = () => {
		const urlParams = new URLSearchParams(location.search);
		return urlParams.get("movieSeq");
	}
	const searchDirectory = (keyword) => {
		$.ajax({
			type: 'GET',
			url: '/admin/search/director',
			data: {keyword: keyword},
			success: (directors) => {
				console.log(directors)
				
				$(".searchbox").css('display', 'block');
				directors.forEach((e)=>{
					$(".searchbox").append(
						`<div class="item" data-actor="${e.actor_seq}">${e.actor_name}</div>`
					)	
				})
				if(directors.length === 0){
					$(".searchbox").append("없음");
				}
			}
		})
	}
	/**
	 * 사진 양식을 초기화함
	 * @param clearForm 이 true 이면 사진 양식을 초기화함. false이면 버튼을 해제함
	 */
	const disabledBtn = (clearForm) =>{
		const $file = $('.director-form .actor_pic');
		const $btn = $('.director-form .btn-pic-edit');
		if (clearForm) {
			$file.val('')
			$btn.prop('disabled', true);
		} else {
			$btn.prop('disabled', false);
		}
	}
	$("#director").on("keyup",(e)=>{
		 console.log(e.key)
		 if(e.key === 'Enter') {
		 	let director = $("#director").val();
		 	searchDirectory(director)
		 }	
	 })
	
	$("#addMovie").on("click", (e) => {
		console.log(e);
		let title = $("#title").val();
		let movieBaseInfo = $("#movieBaseInfo").val();
		let producer = $("#producer").val();
		let openDate = $("#openDate").val();
		let runningTime = $("#runningTime").val();
		let grade = $("#grade :selected").val();
		console.log(grade);
		
		let movieStory = $("#movieStory").val();
		
		$.ajax({
			type : "POST",
			url: `/admin/movie`,
			data:{
			    directorSeqs: `${directorSeq}`,
				title: title,
				movieBaseInfo:movieBaseInfo,
				producer: producer,
				openDate:openDate,
				grade:grade,
				movieStory : movieStory,
				runningTime: runningTime,
			},
			success(res){
				console.log(res)
			},
		})
	})
	
	$("#new-director").on("click",(e)=>{
		$(".modal").css("display","block");
	});
	
	$(".director-form")
	.on("click", '.btn-close' ,(e)=>{
		$(".modal").css("display","none");
	})
	.on("click" ,".btn-name-edit" , (e) => {
		// 실명 수정
		const $form = $('.director-form')
		const $name = $form.find('.actor_name')
		let name = $name.val().trim();
		if (name === '') {
			alert('이름 없음')
			return
		}
		$.ajax({
			type : "PUT",
			url: `/admin/actor/${editingActor.actor_seq}/actorName`,
			data:{
			    name: name,
			},
			success(actor){
				// editingActor.actor_name = actor.actor_name
				const { actor_name } = actor
				actors
					.filter((e) => e.actor_seq === editingActor.actor_seq)
					.forEach((actor) => {
						actor.actor_name = actor_name
					})
				renderActors2();
			},
		})
	})
	.on("click",".btn-casting-edit",(e)=>{
		// 배역 정보 수정
		const actorSeq = editingActor.actor_seq
		const prevRole = editingActor.casting_role
		const movieSeq = getMovieSeq();
		const $form = $('.director-form')
		
		const $casting = $form.find('.casting')
		const $role = $form.find('.role')
		
		let casting = $casting.val().trim();
		let newRole = $role.val().trim();
		
		$.ajax({
			type : "POST",
			url: `/admin/casting/actorUd`,
			data:{
				actorSeq,
				movieSeq,
			    castingName : casting,
			    prevRole ,
			    newRole
			},
			success(casting){
				console.log(casting.castingName)
				editingActor.casting_name = casting.castingName
				editingActor.casting_role = casting.castingRole
				
				renderActors(
					$('.page .casting'),
					 actors.filter((casting)=>casting.casting_role !== "D"))
			},
		})
	})
	.on("click",(e)=>{
		const $target =$(e.target)
		const $form = $('.director-form')
		if ($target.is('.director-form')) {
			$(".director-form").css("display","none");
			//console.log("dddddd");
		}  
	})
	.on('change','.actor_pic',(e)=>{
		disabledBtn(false);
	})
	.on('click','.btn-pic-edit',(e) =>{
		const $file =$(".actor_pic");
		console.log($file[0].files[0]);
		const actorSeq = editingActor.actor_seq
		//const actorPic = $('#pic').val()
		const form = new FormData();
		form.append("file",$file[0].files[0]);
		$.ajax({
			method : 'POST',
			url:`/admin/actor/${actorSeq}/pic`,
			data: form,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success(res){
				//console.log(res.actor_name);
				//$('.actor-pic').attr('src', `/detailImg/${res.actor_pic}`)
				//editingActor.actor_pic = res.actor_pic
				editingActor.updated = true
				const dirs = actors.filter((dir)=> actorSeq === dir.actor_seq)
				console.log(dirs);
				dirs.forEach((actor)=>{
					actor.actor_pic = res.actor_pic
				})
				
				disabledBtn(true);
				renderActors2();
				/*if(!res.actor_pic){
					$(".btn-pic-edit").css('disabled',true);
				}*/
			},
		})
	})
	/**
	 * 네비게이션 버튼
	 */
	.on('click','.nav-btn',(e)=>{
		console.log(e.tartget)
		// 1. 전체 actors 중에서 배우들만 추려냄
		const list = actors.filter((actor)=>  actor.casting_role != 'D')
		// 2. list 안에서 editingActor 의 index(0, 1, 2, ....)
		const idx = list.findIndex((elem)=> elem.actor_seq === editingActor.actor_seq)
		
		let nextEditingActor = undefined;
		nextEditingActor = list[idx + 1] // 6
		if(!nextEditingActor){
			nextEditingActor = list[0];
		}
		showActorEditor(nextEditingActor);
		console.log(nextEditingActor)
		
	})
	.on('click','.nav-btn-prev',(e)=>{
		console.log(e.tartget)
		// 1. 전체 actors 중에서 배우들만 추려냄
		const list = actors.filter((actor)=>{
			const isActor = actor.casting_role != 'D'
			return isActor;
		})
		// 2. list 안에서 editingActor 의 index(0, 1, 2, ....)
		const idx = list.findIndex((elem)=>{
			return elem.actor_seq === editingActor.actor_seq
		})
		
		console.log(idx, list)
		let nextEditingActor = undefined;
		
		nextEditingActor = list[idx - 1] // 6
		
		let prevIdx = list.length - 1 
		if(!nextEditingActor){
			nextEditingActor = list[prevIdx];
		}
		showActorEditor(nextEditingActor);
		console.log(nextEditingActor)
		
	})
	;
	
	/*
		감독 수정시 아래와 같이
		renderActors(
				$('.page .directors'),
				 actors.filter((actor)=>actor.casting_role === "D"))
		*/
	const roleMap = new Map();
	roleMap.set('L', '주연')
	roleMap.set('S', '조연')
	roleMap.set('E', '단역')
	roleMap.set('D', '감독')
	const roleText2 = (role) => {
		const name = roleMap.get(role)
		return name || `오류${role}`
	}
	
	const roleText = (role) => {
		if(role === "L"){
			return "주연";
		} else if(role === "S"){
			return "조연";
		} else if(role === "E"){
			return "엑스트라";
		} else if(role === "D"){
			return "감독";
		} 
		else {
			// 방어적으로
			return `오류${role}`;
		}
	}
	
	const renderActors = ($parentEl, actors) => {
		$parentEl.empty()
		actors.forEach((actor)=>{
			const name = actor.actor_name;
			const castingName = actor.casting_name;
			const role = actor.casting_role
			$parentEl.append(`
          		<div class="actor ${actor.updated ? 'updated':  ''}" data-here="dddd">
          			<div class="thumbnail" style="background-image: url(/detailImg/${actor.actor_pic})">
       					<label><input class="actor-form" type="file" data-file="${actor.actor_seq}"></label>
          			</div>
          			<div class="detail">
          				<span>[${ roleText(role) }]</span>
          				<div>
          					<span>${name}</span>
	          				<span class="role">${castingName}</span>
	          			</div>
	          			<div>
	          				<button class="actor-edit" data-actor="${actor.actor_seq}">수정</button>
          					<button class="actor-delete" data-actor="${actor.actor_seq}">삭제</button>
	          			</div>
	          			
          			</div>
          			
          		</div>
          	`);
		});
	}
	
	const renderActors2 = () => {
		renderActors($('.page .directors'),
			actors.filter((actor)=>actor.casting_role === "D"))		 
		renderActors($('.page .casting'),
			actors.filter((actor)=>actor.casting_role !== "D"))
	}
	
	/**
	 * 배역 삭제 - 감독 또는 출연진을 영화에서 삭제함
	 */
	const deleteCasting = (movieSeq ,actorSeq) => {
		$.ajax({
			url:`/admin/casting/movie/${movieSeq}/actor/${actorSeq}`,
			method:`DELETE`,
			success(actor){
				console.log(actor);
				const idx = actors.findIndex((casting)=>casting.actor_seq ===  actor.actorSeq)
				actors.splice(idx,1);
				console.log(actors)
				//actor.put
				//renderActors($(".page .directors"), actors.filter((actor) => actor.casting_role === 'D'))
				//renderActors($(".page .casting"), actors.filter((actor) => actor.casting_role !== 'D'))
				renderActors2();
			}	
		})
	}
	const movieSeq = getMovieSeq();
		
	
	/**
	 * 감독 리스트
	 */
	$(".page .directors")
	.on("click",".actor-edit",(e)=>{
		const director = actors.find((dir) => 
			dir.actor_seq === $(e.target).data("actor")
			&&  dir.casting_role === "D" )
		editingActor = director
			
		const $modal = $('.modal.director-form');
		$modal.find(".actor_name").val(director.actor_name)
		$modal.find(".casting-list").hide()
		$modal.find('.actor_pic').val('')
		disabledBtn(true)
		$modal.show();
	})
	.on("click",".actor-delete",(e) => {
		const movieSeq = getMovieSeq();
		const actorSeq = $(e.target).data("actor");
		deleteCasting(movieSeq, actorSeq)
	})
	/**
	 * 배우(감독) 수정 모달을 띄움
	 */
	const showActorEditor = (actor) => {
		
		editingActor = actor
		const $modal = $('.modal.director-form');
		
		$modal.find('.actor_name').val(actor.actor_name)
		$modal.find('.casting').val(actor.casting_name)
		$modal.find('.role').val(actor.casting_role)
		$modal.find('.actor_pic').val('')
		disabledBtn(true)
		// $modal.find('.actor_seq').val(actor.actor_seq)
		$(".casting-list").show()
		$modal.show();
	}
	/**
	 * 배우 리스트
	 */
	$('.page .casting')
	.on('click', '.actor-edit', (e)=>{
		console.log(actors);
		
		const actor = actors.find((actor) => 
			actor.actor_seq === $(e.target).data("actor")
			&&  actor.casting_role !== "D" )
		console.log(actor)
		showActorEditor(actor)
	})
	.on('click','.actor-delete',(e)=>{
		const movieSeq = getMovieSeq();
		const actorSeq = $(e.target).data("actor");
		console.log(movieSeq)
		console.log(actorSeq)
		deleteCasting(movieSeq, actorSeq)
		/*
		$.ajax({
			url:`/admin/casting/movie/${movieSeq}/actor/${actorSeq}`,
			method:`DELETE`,
			success(actor){
				console.log(actor);
				const idx = actors.findIndex((casting)=>casting.actor_seq ===  actor.actorSeq)
				actors.splice(idx,1);
				console.log(actors)
				//actor.put
				
				renderActors($(".page .casting"), actors.filter((actor) => actor.casting_role !== 'D'))
			}
		})
		*/
	})
	.on('change', '.actor-form', (e)=>{
		const actorSeq = $(e.target).data("file")
		console.log(actorSeq)
		
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
				const _actors = actors.filter((actor)=>actor.casting_role !== "D");
				const actor = _actors.find((ac)=>actorSeq === ac.actor_seq)
				console.log('[배우]', actor)
				actor.actor_pic = res.actor_pic
				renderActors($('.page .casting'), _actors)
			},
		})
	})
	const renderSearchActors = ($parentEl, actors) => {
		$parentEl.empty()
		actors.forEach((actor)=>{
			$parentEl.append(`<button class="actor" data-actor="${actor.actor_seq}">${actor.actor_name}</button>`)
		})
	}
	
	const resetCastingForm = (closeModal) => {
		const $form = $('.casting-reg .reg-form')
		$form.find("#actor").val('');
		$form.find('.actors').empty();
		
		$form.find(".casting").val('');
		$form.find(".role").val('L');
		$form.find('.casting-list').hide();
		$form.find('.actor_pic').val('');
		if (closeModal) {
			$('.casting-reg').hide()
		}
	}
	$('.casting-reg')
	.on('click',(e)=>{
		// target: 진짜로 눌린 애
		// currentTarget: 리스너가 걸린 애
		if (e.target === e.currentTarget) {
			console.log('빽판 클릭')
			resetCastingForm(true)
		}
	})
	.on('click', '.actor',  (e) => {
		targetActor = $(e.target).data('actor')
		console.log('[button]', targetActor)
		$('.casting-list').show()
		
	})
	.on('click', '.btn-reg', (e) => {
		console.log(e);
		// targetActor = $(e.target).data('actor')
		// 배역 등록
		console.log(targetActor);
		// const moveSeq = 1 // ????
		const castingName = $('.casting-reg .casting').val();
		const role = $('.casting-reg select').val();
		console.log('[배역 등록 여기서 날림]', movieSeq)
		$.ajax({
			type:"POST",
			url:`/admin/movie/${movieSeq}/actor/${targetActor}`,
			data:{
				castingName: castingName,
				role: role
			},
			success(actor){
				console.log(actor);
				$('.casting-reg').hide();
				
				actors.push(actor)
				renderActors2()
				resetCastingForm(true)
			},
		});
	})
	.on('click','.btn-close',(e) =>{
		$('.casting-reg').hide();
		resetCastingForm(true)
	}) // END .casting-reg
	
	$("#addActor").on('click',(actor)=>{
		//$actors.empty();
		let targetActor = undefined
		$('.casting-reg').show();
		
		$('#actor').keyup((e) => {
			if(e.keyCode == '13'){
				$('.casting-list').hide()
				let keyword = $('#actor').val();
				$.ajax({
					type:"GET",
					url:`/admin/movie/searchActor/${keyword}`,
					success(actors){
						renderSearchActors($('.actors'), actors)
						if(actors.length === 0) {
							$('.actors').append(`[${keyword}]가 없습니다. <button class="add">바로 등록</button> `)
						}
						const actorName = keyword
						$('.add').on('click',(e)=>{
							// 배우를 신규 등록
							$.ajax({
								type:"POST",
								url:`/admin/actor/easy`,
								data:{
									actorName
								},
								success(actor){
									console.log(actor);
									renderSearchActors($('.actors'), [actor])
								},
							});
						})
					},
				});	
			}
		})
	})
	
	$('.directors').on('click__',(e)=>{
		const $file =$(".actor_pic");
		console.log($file);
		console.log($file[0].files[0]);
		//const actorSeq = editingActor.actor_seq
		const actorSeq = e.actor_seq
		//const actorPic = $('#pic').val()
		const form = new FormData();
		form.append("file",$file[0].files[0]);
		
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
	}).change("input[type='file']",(e)=>{
		const actorSeq = $(e.target).data("file")
		console.log(actorSeq)
		
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
				
				const dirs = actors.filter((dir)=> actorSeq === dir.actor_seq)
				console.log(dirs);
				dirs.forEach((actor)=>{
					actor.actor_pic = res.actor_pic
				})
				const directors = actors.filter((actor)=>actor.casting_role === "D");
				const _actors = actors.filter((actor)=>actor.casting_role !== "D");
				
				renderActors($('.page .directors'), directors)
				renderActors($('.page .casting'), _actors)
			},
		})
	})
	
	
	// 출연진 목록 가져오기
	$.ajax({
		type:"GET",
		url:`/admin/movie/actorNameInfo/${movieSeq}`,
		success(res){
			actors = res
			renderActors2();
		},
		error(){
			
		}, 
	})
	/**
	 * 영화 포스터 렌더링
	 */
	const renderMoviePoster = (movie)=>{
		movie.movie_poster
		console.log(movie.movie_posters) // [a, b]
		// const posters = [e.movie_poster, e.movie_poster, e.movie_poster]
		let maxPoster = movie.movie_posters[0] // MoviePoster maxPoster
		movie.movie_posters.forEach((poster)=>{
			if(poster.mainPoster > maxPoster.mainPoster){
				maxPoster = poster;  
			}
		})
		
		console.log('[max]', maxPoster)
		
		$(".posters").empty()
		movie.movie_posters.forEach((poster)=>{
			$(".posters").append(`<div
			class="thumbnail poster" 
			style="background-image: url(/detailImg/${poster.moviePath})">
				${ maxPoster === poster ? `<span class="main">대표</span>` : ''}
				<button data-poster="${poster.posterSeq}">삭제</button>
			</div>
			`)
		})
	}
	$('.page .posters')
	.on('click', '.thumbnail button', (e) => {
		const posterSeq = $(e.target).data('poster')
		$.ajax({
			type: 'DELETE',
			url: `/admin/movie/${editingMovie.movie_seq}/poster/${posterSeq}`,
			success(res) {
				/**
				 * 1. posterSeq 에 해당하는 포스터를 찾아서 editingMovie.movie_posters 안에서 지워야 함
				 */
				console.log(res)
				const idx = editingMovie.movie_posters.findIndex((poster)=>{
					return poster.posterSeq === posterSeq;
				})
				editingMovie.movie_posters.splice(idx,1);
				renderMoviePoster(editingMovie);
			}
		})
	})
	.on('click','.poster',(e) =>{
		const posterSeq = $(e.target).find('button').data('poster');
		$.ajax({
			type: 'PUT',
			url: `/admin/movie/${editingMovie.movie_seq}/mainPoster/${posterSeq}`,
			success(res) {
				// editingMovie.movie_posters.splice(idx,1);
				
				const activePoster = editingMovie.movie_posters.find((e)=>{
					return e.posterSeq === posterSeq
				})
				activePoster.mainPoster = res
				renderMoviePoster(editingMovie);
			}
		})
	})
	
	
	
	$(".page")
	.on('change','.poster',(e)=>{
		console.log(e);
		
		
		const movieSeq = getMovieSeq();
		console.log(movieSeq)
		
		const form = new FormData();
		form.append("file",e.target.files[0]);
		
		$.ajax({
			method : 'POST',
			url:`/admin/movie/${movieSeq}/poster`,
			data: form,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
		    cache: false,
			success(moviePoster){
				editingMovie.movie_posters.push(moviePoster)
			    renderMoviePoster(editingMovie);
				
			},
		})
		
	})
	// 영화 기본 정보 가져오기
	$.ajax({
		type:"GET",
		url:`/admin/movie/${movieSeq}/basic`,
		success(movie){
			console.log(movie)
			editingMovie = movie;
			renderMoviePoster(movie)
		},
		error(){
			
		}, 
	})
	
});