/**
 * 
 */
 
 const api = {
	user: {
		udapteNick: () => {},
		updatePassword: () => {}
	},
	review: {
		/**
		 * 새로운 리뷰 등록
		 */
		write: () => {},
		/**
		 * 리뷰 수정
		 */
		update: () => {},
		/**
		 * 사용자가 작상했던 리뷰 삭제
		 * @param {number} reviewSeq - 삭제할 리뷰 PK
		 * @param {(success) => void} callback - 성공 시 callback
		 */
		delete: (reviewSeq, callback) => {
			$.ajax({
				url: `/review/${reviewSeq}`,
				method: `DELETE`,
				success: callback
				/*success(res){
					callback(res)
				}*/
			})
		}
	}
}
window.api = api