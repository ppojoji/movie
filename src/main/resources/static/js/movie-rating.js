$(document).ready(() => {
	const urlParams = new URLSearchParams(window.location.search);
	const movieSeq = urlParams.get("movieSeq");
	
	
	const renderRatingView = ($parent, reviews) => {
		$parent.append('<h3>리뷰</h3>')
		const $textbox = $(`<div class="review-box">
			<textarea></textarea>
			<div>
				<select class="selectBtn">
					<option value="1">1점</option>
					<option value="2">2점</option>
					<option value="3">3점</option>
					<option value="4">4점</option>
				</select>
				<button class="reviewInsert">등록</button>
			</div>
		</div>`).appendTo($parent)
		const $reviews = $('<div class="reviews"></div>').appendTo($parent)
		reviews.forEach((review)=>{
			$reviews.append(`<div class="review">
			<h5>
				<span class="user">${review.user_id}</span>
				<span class="score">${review.rv_score}</span>
			</h5>
			<p>${review.rv_comment}</p>
			</div>`
			)	
		})
		
	}
	
	$('#movieStory')
	.on('click', '.reviewInsert', (e) => {
		
		const movie_ref = movieSeq;
		const user_ref = 2;
		const rv_score = $(".selectBtn option:selected").val();
		const comment = $('.review-box > textarea').val()
		
		console.log(movie_ref);
		console.log(user_ref);
		console.log(rv_score);
		console.log(comment);
		
		const form = new FormData()
		form.append('movie_ref', movie_ref);
		form.append('user_ref',user_ref);
		form.append('rv_score',rv_score);
		form.append('rv_comment',comment);
		
		$.ajax({
			method : "post",
			url : `/movie/writeReview`,
			data:form,
			contentType: false,
            processData: false,
			success(res){
				console.log(res);
			}
		})
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
			success(reviews){
				renderRatingView($body, reviews)
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


