/**
 * mypage/index.js
 * 사용자 정보 파일
 */
$(document).ready(()=>{
	let reviews = [];
	let loginUser;
	
	const renderUserInfo = ($section, user) => {
		// user.user_id,
		// user.user_nic
		// user.use_pass
		$section.empty();
		$section.append(`<ul>
			<li>${user.user_nic}</li> 
			<button class="nic">닉네임 변경</button>
		</ul>`)
	}
	const renderUserReview = ($section, reviews) => {
		$section.empty();
		const $ul = $('<ul></ul>').appendTo($section) // .append(`<ul></ul>`)
		reviews.forEach((re)=>{
			const $li = $(`
			<li>
				<p>
					<span class="review">${re.rv_comment}</span>
					<span class="score">${re.rv_score}</span>
					<span class="movie-title">${re.movie.movie_title}</span>
				</p>
				<div class="star-box">
					<!--<div class="scoring">
					</div>-->
				</div>
				<div class="posters">
					<!--<img src="/detailImg/d3319981-3ae4-43d9-82cc-fe05e52967d7.jpeg" width="48"/>-->
				</div>
			<button class="rv_delete" data-rv="${re.rv_seq}">삭제</button></li>`).appendTo($ul)
			
			const $starBox = $li.find('.star-box')
			scoring.renderStars($starBox, re.rv_score)
			// $scoring.replaceWith('<section>되나?</section>')
			
		    const $posters = $li.find('.posters')
		    re.movie.movie_posters.forEach((poster)=>{
				$(`<img src="/detailImg/${poster.moviePath}" width="48"/>`).appendTo($posters);
			})
			
		    console.log($posters);
		})
	}
	const loadUserInfo = () => {
		$.ajax({
			url: `/userInfo`,
			method:"GET",
			success(res){
				console.log(res);
				const $section = $('.tab-body > .active')
				console.log($section);
				loginUser = res;
				renderUserInfo($section, res)
				
			}
		}) 
	}
	const loadUserReview = () => {
		$.ajax({
			url: `/userReview`,
			method:"GET",
			success(reviewList){
				reviews = reviewList
				const $section = $('.tab-body > .community')
				console.log($section);
				renderUserReview($section, reviewList)
			}
		}) 
	}
	
	
	$('.tab-header')
	.on('mouseover','button',(e) =>{
		// console.log(e);
	})
	.on('click','button',(e) => {
		const $button = $(e.target)
		$('.tab-header > button.active').removeClass('active');
		$button.addClass('active')
		
		const tabName =$button.data('tab') // "basic", "community", "note"
		$('.tab-body>section').removeClass('active')
		$(`.tab-body > section[data-tab='${tabName}']`).addClass('active');
		
		if(tabName === 'basic'){
			loadUserInfo();
		}else if(tabName === 'community'){
			loadUserReview();	
		}
	})
	
	const template = {
		modal : `<div class="nick-modal" style="display: none;">
			<div class="dimmer"></div>
			<div class="body">
				<h3>닉네임 변경</h3>
				<div><input type="text" class=nickname></div>
				<div><button class="change-nick">변경</div>
			</div>
		</div>`
	}
	const $modal = $(template.modal).appendTo('body')
	
	const showNickModal = () => {
		$modal.find('.nickname').val(loginUser.user_nic);
		$modal.show();
	}
	const hideModal = () => {
		$modal.hide();
	}
	$modal
	.on('click', '.dimmer', () => {
		hideModal();
	})
	$('.tab-body')
	.on('click','.rv_delete',(e) =>{
		console.log(e);
		rvSeq = $(e.target).data('rv');
		api.review.delete(rvSeq, (res) => {
			console.log(res)
			const idx = reviews.findIndex((review)=>{
				return review.rv_seq === rvSeq
			})
			reviews.splice(idx,1);
			const $section = $('.tab-body > .community')
			renderUserReview($section, reviews);
		})
		
	})
	.on('click','.nic',(e)=>{
		showNickModal();
	})
});