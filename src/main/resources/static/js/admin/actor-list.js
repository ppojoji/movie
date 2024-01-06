$(document).ready(()=>{
	
	const renderActor = (lastActorSeq, clearList) => {
		
		if (clearList) {
			$("#more").show();
			$(".actor-list").empty();
		}
		// const lastActorSeq = $(".actor-list .actor:last-child").data('actor');
		const role = $("input[name=role]:checked").val();
		let size = $("select[name='listPageSize']").val();	
		$.ajax({
			type:"GET",
			url:`/admin/actors`,
			data:{lastActorSeq, size, role},
			success(actors){
				console.log(actors);
				actors.forEach((actor)=>{
	        		let html = `<div class="actor" data-actor="${actor.actor_seq}">
		        		<div class="name">${actor.actor_name}</div>
			    		<div class="actor-pic"><img src="/detailImg/${actor.actor_pic}"></div>
		        		<a href="/admin/edit/actor?actorSeq=${actor.actor_seq}">수정</a>
		        	</div>`
					$(".actor-list").append(html);
					
				})
				
				const total = $(".actor-list .actor").length
				$(".more .all-search span").text(total);
				sessionStorage.setItem("total", total);
				if(actors.length === 0){
					alert("마지막 입니다.");
					$('#more').hide();
				}
			},
		});
	}
	
	$("#more").on('click',(e)=>{
		const lastActorSeq = $(".actor-list .actor:last-child").data('actor');
		console.log("[lastActorSeq]" + lastActorSeq)
		renderActor(lastActorSeq)
	});
	
	const total = Number.parseInt(sessionStorage.getItem('total'));
	const basicTotal = $('.actor-list .actor').length;
	const moreSize = total - basicTotal
	
	if(moreSize > 0 ){
		renderActor(moreSize);
	}
	
	$("input[name=role]").on('change',(e)=>{
		const lastActorSeq = -1
		renderActor(lastActorSeq, true)
	})
});