$(document).ready(() => {
	const urlParams = new URLSearchParams(window.location.search);
	const movieSeq = urlParams.get("movieSeq");
	let reviews =[];
	let loginUser;
	let editingReview; // 
	
	const getReviewEditForm = (review) => {
		if(!loginUser){
			return '';
		}
		
		if(loginUser.user_id === review.user_id){
			return `<p><button class="reviewUdBtn" data-ud="${review.rv_seq}">수정</button><button class="reviewDelBtn" data-del="${review.rv_seq}">삭제</button></p>`
		} else {
			return ''
		}
	}
	
	const renderReviewBox = ($parent, editMode) => {
		const $textbox = $(`<div class="review-box">
			<div class="scoring">
				<!--<button class="star" data-score="2"><img src="/image/star_off.svg"></button>-->
			</div>
			<div>
				<textarea></textarea>
				<input type="hidden" class="selectBtn" value="">
				${editMode === true ? '<button class="reviewUpdate">편집</button>' : '<button class="reviewInsert">등록</button>'}
				${editMode === true ? '<button class="reviewCancel">취소</button>' : ""}
			</div>
		</div>`).appendTo($parent)
		
		const $starBox = $textbox.find('.scoring')
		for(let score=1 ; score <= 10; score ++) {
			$starBox.append(`<button class="star" data-score="${score}"><img src="/image/star_off.svg"></button>`)
		}
		return $textbox;
	}
	const renderRatingView = ($parent, movie, reviews) => {
		$parent.append(`<h3 class="rating"><span class="label">리뷰</span><span class="avg">${movie.movie_avg_score.toFixed(2)
}</span></h3>`)
		renderReviewBox($parent)
		const $reviews = $('<div class="reviews"></div>').appendTo($parent)
		renderRenderReview($reviews, movie, reviews);
	}
	
	const showReviewEditor = (review) => {
		return `<div class="rv-edit-form">
			<textarea>${review.rv_comment}</textarea>
		</div>`
	}
	const renderRenderReview = ($reviews, movie, reviews) => {
		$('#movieStory .rating .avg').text(movie.movie_avg_score.toFixed(2));
		$reviews.empty()
		reviews.forEach((review)=>{
			$reviews.append(`<div class="review">
			<h5>
				<span class="user">${review.user_id}</span>
				<span class="score">${review.rv_score}</span>
			</h5>
			<p>${review.rv_comment}</p>
			${ getReviewEditForm(review) }
			<div class="rv-edit-form"></div>
			</div>`
			)
		})	
	}
	
	const showReviwEditForm = ($btn, review) => {
		const $review = $btn.closest('.review')
		const $editForm = $review.find('.rv-edit-form')
		console.log($editForm);
		$editForm.show();
		const $box = renderReviewBox($editForm, true)
		console.log($box)
		$box.find('textarea').val(review.rv_comment)
		$box.find('.selectBtn').val(review.rv_score)
		
		for(let val = 1; val <= review.rv_score; val++){
			const $star = $box.find(`[data-score="${val}"]`)
			$star.find("img").attr('src', '/image/star_on.svg')
		}
		
		// const imgBtn = $box.find('.score').attr('src','/image/star_off.svg')
		// $box.find('.score').val($box.find('.score').attr('src','/image/star_off.svg'))
		// const review.find(".rv-edit-form")
	}
	// clearScoring(???)
	const clearScoring = ($reviewBox) => {
		const $textarea = $reviewBox.find('textarea')
		const $scoring = $('.scoring .star', $reviewBox)
		const $selectBtn = $('.selectBtn')
		
		console.log($textarea);
		console.log($scoring);
		
		$textarea.val('');
		$scoring.find("img").attr('src', '/image/star_off.svg')
		$selectBtn.val('');
	}
	$('#movieStory')
	.on('click', '.reviewInsert', (e) => {
		
		const movie_ref = movieSeq;
		const rv_score = $(".selectBtn").val();
		const comment = $('.review-box > div > textarea').val()
		
		console.log(movie_ref);
		console.log(rv_score);
		console.log(comment);
		// "", 0, undefined, null
		if(!rv_score) {
			alert("점수를 입력하세요");
			return
		}
		if(!comment){
			alert("리뷰를 입력하세요");
			return;
		}
		const form = new FormData()
		form.append('movie_ref', movie_ref);
		form.append('rv_score',rv_score);
		form.append('rv_comment',comment);
		
		$.ajax({
			method : "post",
			url : `/movie/writeReview`,
			data:form,
			contentType: false,
            processData: false,
			success(res){
				clearScoring($('#movieStory > .review-box'))
				
				const { avg, review } = res
				console.log(review);
				const $reviews =  $('#movieStory > .reviews')
				
				reviews.unshift(review);
				movie.movie_avg_score = avg
				renderRenderReview($reviews, movie , reviews);
			},
			error(e) {
				alert('로그인하세요')
				console.log('[터짐]', e)
			}
		})
	})
	.on('click','.scoring .star', (e) =>{
		const $btn = $(e.target).closest('.star');
		const score = $btn.data("score")
		const $box = $btn.closest(".scoring");
		
		for(let val = 1; val <= score; val++){
			const $star = $box.find(`[data-score="${val}"]`)
			$star.find("img").attr('src', '/image/star_on.svg')
		}
		for(let val = score +1; val <= 10; val++ ){
			const $star = $box.find(`[data-score="${val}"]`)
			$star.find("img").attr('src', '/image/star_off.svg')
		}
		// FIXME: 등록과 수정의 값이 둘다 바뀜
		$(".selectBtn").val(score);
	})
	.on('click','.reviewUdBtn',(e) =>  {
		const $btn = $(e.target)
		const reviewSeq = $btn.data('ud')
		console.log('[reviews seq]', reviewSeq)
		const review = reviews.find((review)=>{
			return review.rv_seq === reviewSeq
		})
		editingReview = review; 
		console.log(review)
		showReviwEditForm($btn, review)
		
	})  
	.on('click','.reviewUpdate',(e) =>  {
		$btn = $(e.target);
		const $box = $btn.closest('.review-box')
		const comment = $box.find('textarea').val()
		const score = $box.find('.selectBtn').val()
		const reviewSeq = editingReview.rv_seq;
		console.log(reviewSeq, comment, score)
		
		$.ajax({
			url : `/review/${reviewSeq}`,
			method : "PUT",
			data: {
				comment,
				score
			},
			
			success(res){
				// res: { review: {...}, avgScore: 4.5 }
				console.log(res)
				const $btn = $(e.target)
				const $rvEditForm = $btn.closest('.rv-edit-form')
				$rvEditForm.hide();
				const editingReview = reviews.find((re)=>{
					if(res.review.rv_seq === re.rv_seq){
						return true;
					}
				})
				editingReview.rv_comment = res.review.rv_comment;
				editingReview.rv_score = res.review.rv_score;
				movie.movie_avg_score = res.avgScore;
				const $reviews =  $('#movieStory > .reviews')
				renderRenderReview($reviews, movie , reviews);
			}
		})
	})
	.on('click','.reviewCancel',(e) =>  {
		const $btn = $(e.target)
		const $rvEditForm = $btn.closest('.rv-edit-form')
		console.log($rvEditForm);
		$rvEditForm.empty();
		$rvEditForm.hide();
	})
	.on('click','.reviewDelBtn',(e) => {
		console.log('삭제');	
		
		if(confirm("리뷰를 삭제 하시겠습니까?")){
			//const reviewSeq = editingReview.rv_seq;
			const $btn = $(e.target)
			const reviewSeq = $btn.data('del')
			const review = reviews.find((review)=>{
				return review.rv_seq === reviewSeq
			})
			editingReview = review; 
			
			api.review.delete(reviewSeq,(res)=>{
				const idx = reviews.findIndex((r)=>{
					if(res.review.rv_seq === r.rv_seq){
						return true;
					}
				})
				reviews.splice(idx,1);	
				movie.movie_avg_score = res.avgScore;
				const $reviews =  $('#movieStory > .reviews')
				renderRenderReview($reviews, movie , reviews);
			}) 
		}
	})
	
	$('.tab')
  	.on('click', '[data-type="movie_story"]', (e) => {
		const $body = $('#movieStory')
		$body.empty();
		
		$.ajax({
			method : "GET",
			url : `/movie/${movieSeq}/detail?type=movie_story`,
			success(res){
				let html = res.value.replaceAll('\r\n','<br/>');
				$("#movieStory").append(html);
			}
		})
	})
	.on('click', '[data-type="actor"]', (e) => {
		
		const $body = $('#movieStory')
		$body.empty();
		
		$.ajax({
			method : "GET",
			url : `/movie/${movieSeq}/detail?type=actor`,
			success(res){
				const actors = res.value
				const $director = $(`
					<div>
						<h3>감독</h3>
						<div class="picholder"></div>
					</div>`).appendTo($body).find('.picholder')
				let dir = actors.filter((actor)=> actor.casting_role === "D")
				dir.forEach((actor) => {
					const name = actor.actor_name;
					const castingName = actor.casting_name;
					const actorPic = actor.actor_pic
					
					
					$director.append(`<div class="actor">
					    <img src="/detailImg/${actorPic}"></img> 
					    <div class="info">
					    	<span>${name}</span>
					    </div>
					</div>`);
				})
				
				const $picholder =  $(`
					<div>
						<h3>배우</h3>
						<div class="picholder"></div>
					</div>`).appendTo($body).find('.picholder')
				actors
					.filter((actor)=>actor.casting_role !== "D")
					.forEach((actor)=>{
					const name = actor.actor_name;
					const castingName = actor.casting_name;
					const actorPic = actor.actor_pic
					let castingRole = actor.casting_role;
					
					let roleText = '';
					if(castingRole === 'L'){
						roleText = '주'; 
					}else if(castingRole === 'S'){
						roleText = '조';
					}else {
						roleText = '단';
					}
					$picholder.append(`<div class="actor">
						<div class="role ${castingRole}">${roleText}</div>
					    <img src="/detailImg/${actorPic}"></img> 
					    <div class="info">
					    	<span><a href="/actor?actorSeq=${actor.actor_seq}">${name}</a></span>
							<span>${castingName}역</span>
					    </div>
					</div>`);
				});
			}
		})
	})
	.on('click', '[data-type="pic"]', (e) => {
		console.log('[pic]')
	})
	.on('click', '[data-type="rating"]', (e) => {
		const $body = $('#movieStory')
		$body.empty();
		$.ajax({
			method : "GET",
			url : `/movie/${movieSeq}/reviews`,
			success(res){
				loginUser = res.loginUser
				movie = res.movie
				reviews = res.reviews
				renderRatingView($body, movie, reviews)
			}
		})
	})
	
  $(".xtab-item").click(function (e) {
	const urlParams = new URLSearchParams(window.location.search);
	let movie = urlParams.get("movieSeq");
	console.log(movie)
	let type = $(e.target).closest('.tab-item').data("type");
	console.log(type);
	$.ajax({
		type:"GET",
		url:`/movie/${movie}/detail?type=${type}`,
		success(res){
			
			const $body = $('#movieStory')
			$body.empty();

					
			if(type === "movie_story"){
				let html = res.value.replaceAll('\r\n','<br/>');
				$("#movieStory").append(html);
			}else if(type === "actor"){
				
				const actors = res.value
				console.log(actors);
				const $director = $(`
					<div>
						<h3>감독</h3>
						<div class="picholder"></div>
					</div>`).appendTo($body).find('.picholder')
				let dir = actors.filter((actor)=> actor.casting_role === "D")
				dir.forEach((actor) => {
					const name = actor.actor_name;
					const castingName = actor.casting_name;
					const actorPic = actor.actor_pic
					
					
					$director.append(`<div class="actor">
					    <img src="/detailImg/${actorPic}"></img> 
					    <div class="info">
					    	<span>${name}</span>
					    </div>
					</div>`);
				})
				
				const $picholder =  $(`
					<div>
						<h3>배우</h3>
						<div class="picholder"></div>
					</div>`).appendTo($body).find('.picholder')
				actors
					.filter((actor)=>actor.casting_role !== "D")
					.forEach((actor)=>{
					const name = actor.actor_name;
					const castingName = actor.casting_name;
					const actorPic = actor.actor_pic
					let castingRole = actor.casting_role;
					
					let roleText = '';
					if(castingRole === 'L'){
						roleText = '주'; 
					}else if(castingRole === 'S'){
						roleText = '조';
					}else {
						roleText = '단';
					}
					$picholder.append(`<div class="actor">
						<div class="role ${castingRole}">${roleText}</div>
					    <img src="/detailImg/${actorPic}"></img> 
					    <div class="info">
					    	<span><a href="/actor?actorSeq=${actor.actor_seq}">${name}</a></span>
							<span>${castingName}역</span>
					    </div>
					</div>`);
				});
			} else if (type === 'rating') {
				console.log('[rating]')
			}
		},
		error(){
			
		}, 
	})
  });
  
 
});


