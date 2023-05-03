package com.hanamja.moa.api.service.post;

import com.hanamja.moa.api.dto.post.request.BoardPostSaveAndEditRequestDto;
import com.hanamja.moa.api.entity.board_category.BoardCategory;
import com.hanamja.moa.api.entity.post.Post;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    /* resolve 함수 - 객체의 id를 validate 한 뒤 resolve 해오는 내부 함수들 */
    BoardCategory resolveBoardCategoryById(Long boardCategoryId);
    Post resolvePostById(Long postId);
    Post resolvePostByIdAndUserId(Long postId, Long userId);

    /* Image 등록 메서드 */
    List<String> registerImagesToPostImage(List<MultipartFile> images);

    /* Controller 에서 사용할 함수들 */
    void registerNewBoardPost(UserAccount userAccount, List<MultipartFile> images, BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto);
    void editBoardPost(Long postId, UserAccount userAccount, List<MultipartFile> images, BoardPostSaveAndEditRequestDto boardPostSaveAndEditRequestDto);
    void deleteBoardPost(Long postId, UserAccount userAccount);

    void likeBoardPost(Long postId, UserAccount userAccount);

    void bookmarkBoardPost(Long postId, UserAccount userAccount);

}
