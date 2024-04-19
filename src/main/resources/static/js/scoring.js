/**
 * 
 */
 
 const scoring = {
	renderStars: ($parent,scoring) => {
		
		const $scoring =  $(`<div class="scoring"></div>`).appendTo($parent)
		for(let score=1 ; score <= 10; score ++) {
			$(`<button class="star" data-score="${score}"><img src="/image/star_off.svg"></button>`).appendTo($scoring)
		}
		
		for(let score=1 ; score <= scoring ; score ++) {
			//$(`<button class="star" data-score="${score}"><img src="/image/star_on.svg"></button>`).appendTo($scoring)
			const $star = $scoring.find(`[data-score="${score}"]`)
			$star.find("img").attr('src', '/image/star_on.svg')
		}
	}
}

window.scoring = scoring