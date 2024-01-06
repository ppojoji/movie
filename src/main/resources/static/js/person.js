$(document).ready(() => {
  $(".tab-item").click(function (e) {
	const urlParams = new URLSearchParams(window.location.search);
	let actor = urlParams.get("actorSeq");
	console.log(actor)
	let type = $(e.target).closest('.tab-item').data("type");
	console.log(type);
	$.ajax({
		type:"GET",
		url:`/actor/${actor}/movies`,
		success(res){
			const $body = $('#actorDetail')
			$body.empty()
			console.log(res);
			const $main = $(
				`<div>
					<h3>필모그래피</h3>
					<div class="picholder"></div>
				 </div>`).appendTo($body) 
			const $picholder = $main.find('.picholder')
			console.log($picholder)
			if(type === "mainInfo"){
				res.forEach((movie)=> {
					let poster = movie.movie_poster
					const movieHtml = `<div class="movie-thumbnail">
						<a href="movies/movie-detail?movieSeq=${movie.movie_seq}"><img src="/detailImg/${movie.movie_poster}"></a>
						<h3>${movie.movie_title}</h3>
						<h4>${movie.casting_name}</h4>
					</div>`	
					$picholder.append(movieHtml)
				})
					
			}
		},
		error(){
			
		}, 
	})
  });
});


