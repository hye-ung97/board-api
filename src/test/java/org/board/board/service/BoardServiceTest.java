package org.board.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.board.board.dto.board.BoardCreateRequest;
import org.board.board.dto.board.BoardCreateResponse;
import org.board.board.dto.board.BoardListResponse;
import org.board.board.entity.Board;
import org.board.board.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

  @Mock private BoardRepository boardRepository;

  @InjectMocks private BoardService boardService;

  private BoardCreateRequest validBoardRequest;
  private Board savedBoard;

  @BeforeEach
  void setUp() {
    validBoardRequest = new BoardCreateRequest("테스트 게시글", "테스트 게시글 내용입니다.");
    savedBoard = new Board("테스트 게시글", "테스트 게시글 내용입니다.", 1L);
    savedBoard.setId(1L);
  }

  @Nested
  @DisplayName("게시글 생성 테스트")
  class CreateBoardTest {

    @Test
    @DisplayName("유효한 정보로 게시글 생성 시 성공")
    void createBoard_WithValidData_ShouldSucceed() {
      // given
      Long memberId = 1L;
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(validBoardRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(1L);
      assertThat(response.getTitle()).isEqualTo("테스트 게시글");
      assertThat(response.getContent()).isEqualTo("테스트 게시글 내용입니다.");
      assertThat(response.getMemberId()).isEqualTo(1L);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("다른 사용자 ID로 게시글 생성 시 성공")
    void createBoard_WithDifferentMemberId_ShouldSucceed() {
      // given
      Long memberId = 2L;
      Board differentMemberBoard = new Board("테스트 게시글", "테스트 게시글 내용입니다.", 2L);
      differentMemberBoard.setId(2L);
      when(boardRepository.save(any(Board.class))).thenReturn(differentMemberBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(validBoardRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(2L);
      assertThat(response.getTitle()).isEqualTo("테스트 게시글");
      assertThat(response.getContent()).isEqualTo("테스트 게시글 내용입니다.");
      assertThat(response.getMemberId()).isEqualTo(2L);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("긴 제목으로 게시글 생성 시 성공")
    void createBoard_WithLongTitle_ShouldSucceed() {
      // given
      String longTitle = "이것은 매우 긴 게시글 제목입니다. 최대 100자까지 입력할 수 있는 제목입니다.";
      BoardCreateRequest longTitleRequest = new BoardCreateRequest(longTitle, "내용");
      Long memberId = 1L;
      Board longTitleBoard = new Board(longTitle, "내용", 1L);
      longTitleBoard.setId(3L);
      when(boardRepository.save(any(Board.class))).thenReturn(longTitleBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(longTitleRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(3L);
      assertThat(response.getTitle()).isEqualTo(longTitle);
      assertThat(response.getContent()).isEqualTo("내용");
      assertThat(response.getMemberId()).isEqualTo(1L);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("긴 내용으로 게시글 생성 시 성공")
    void createBoard_WithLongContent_ShouldSucceed() {
      // given
      String longContent = "이것은 매우 긴 게시글 내용입니다. ".repeat(20) + "최대 1000자까지 입력할 수 있습니다.";
      BoardCreateRequest longContentRequest = new BoardCreateRequest("제목", longContent);
      Long memberId = 1L;
      Board longContentBoard = new Board("제목", longContent, 1L);
      longContentBoard.setId(4L);
      when(boardRepository.save(any(Board.class))).thenReturn(longContentBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(longContentRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(4L);
      assertThat(response.getTitle()).isEqualTo("제목");
      assertThat(response.getContent()).isEqualTo(longContent);
      assertThat(response.getMemberId()).isEqualTo(1L);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("빈 제목으로 게시글 생성 시 성공")
    void createBoard_WithEmptyTitle_ShouldSucceed() {
      // given
      BoardCreateRequest emptyTitleRequest = new BoardCreateRequest("", "내용");
      Long memberId = 1L;
      Board emptyTitleBoard = new Board("", "내용", 1L);
      emptyTitleBoard.setId(5L);
      when(boardRepository.save(any(Board.class))).thenReturn(emptyTitleBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(emptyTitleRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(5L);
      assertThat(response.getTitle()).isEqualTo("");
      assertThat(response.getContent()).isEqualTo("내용");
      assertThat(response.getMemberId()).isEqualTo(1L);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("빈 내용으로 게시글 생성 시 성공")
    void createBoard_WithEmptyContent_ShouldSucceed() {
      // given
      BoardCreateRequest emptyContentRequest = new BoardCreateRequest("제목", "");
      Long memberId = 1L;
      Board emptyContentBoard = new Board("제목", "", 1L);
      emptyContentBoard.setId(6L);
      when(boardRepository.save(any(Board.class))).thenReturn(emptyContentBoard);

      // when
      BoardCreateResponse response = boardService.createBoard(emptyContentRequest, memberId);

      // then
      assertThat(response.getId()).isEqualTo(6L);
      assertThat(response.getTitle()).isEqualTo("제목");
      assertThat(response.getContent()).isEqualTo("");
      assertThat(response.getMemberId()).isEqualTo(1L);
      verify(boardRepository).save(any(Board.class));
    }
  }

  @Nested
  @DisplayName("게시글 조회 테스트")
  class GetBoardByIdTest {

    @Test
    @DisplayName("존재하는 게시글 ID로 조회 시 성공")
    void getBoardById_WithExistingId_ShouldReturnBoard() {
      // given
      Long boardId = 1L;
      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

      // when
      Board result = boardService.getBoardById(boardId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getTitle()).isEqualTo("테스트 게시글");
      assertThat(result.getContent()).isEqualTo("테스트 게시글 내용입니다.");
      assertThat(result.getMemberId()).isEqualTo(1L);
      verify(boardRepository).findById(boardId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 ID로 조회 시 null 반환")
    void getBoardById_WithNonExistingId_ShouldReturnNull() {
      // given
      Long nonExistingId = 999L;
      when(boardRepository.findById(nonExistingId)).thenReturn(Optional.empty());

      // when
      Board result = boardService.getBoardById(nonExistingId);

      // then
      assertThat(result).isNull();
      verify(boardRepository).findById(nonExistingId);
    }

    @Test
    @DisplayName("null ID로 조회 시 null 반환")
    void getBoardById_WithNullId_ShouldReturnNull() {
      // given
      when(boardRepository.findById(null)).thenReturn(Optional.empty());

      // when
      Board result = boardService.getBoardById(null);

      // then
      assertThat(result).isNull();
      verify(boardRepository).findById(null);
    }
  }

  @Nested
  @DisplayName("게시글 목록 조회 테스트")
  class GetBoardsListTest {

    @Test
    @DisplayName("게시글 목록 조회 시 성공")
    void getBoardsList_WithValidPageable_ShouldReturnPage() {
      // given
      List<Board> boards =
          Arrays.asList(new Board("첫 번째 게시글", "첫 번째 내용", 1L), new Board("두 번째 게시글", "두 번째 내용", 2L));
      boards.get(0).setId(1L);
      boards.get(1).setId(2L);

      Pageable pageable = PageRequest.of(0, 10);
      Page<Board> boardPage = new PageImpl<>(boards, pageable, 2L);

      when(boardRepository.findAll(pageable)).thenReturn(boardPage);

      // when
      Page<BoardListResponse> result = boardService.getBoardsList(pageable);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getContent()).hasSize(2);
      assertThat(result.getTotalElements()).isEqualTo(2);
      assertThat(result.getTotalPages()).isEqualTo(1);

      BoardListResponse firstBoard = result.getContent().get(0);
      assertThat(firstBoard.id()).isEqualTo(1L);
      assertThat(firstBoard.title()).isEqualTo("첫 번째 게시글");
      assertThat(firstBoard.content()).isEqualTo("첫 번째 내용");
      assertThat(firstBoard.memberId()).isEqualTo(1L);

      BoardListResponse secondBoard = result.getContent().get(1);
      assertThat(secondBoard.id()).isEqualTo(2L);
      assertThat(secondBoard.title()).isEqualTo("두 번째 게시글");
      assertThat(secondBoard.content()).isEqualTo("두 번째 내용");
      assertThat(secondBoard.memberId()).isEqualTo(2L);

      verify(boardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("빈 게시글 목록 조회 시 빈 페이지 반환")
    void getBoardsList_WithEmptyResult_ShouldReturnEmptyPage() {
      // given
      Pageable pageable = PageRequest.of(0, 10);
      Page<Board> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0L);

      when(boardRepository.findAll(pageable)).thenReturn(emptyPage);

      // when
      Page<BoardListResponse> result = boardService.getBoardsList(pageable);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getContent()).isEmpty();
      assertThat(result.getTotalElements()).isEqualTo(0);
      assertThat(result.getTotalPages()).isEqualTo(0);

      verify(boardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("페이지네이션으로 게시글 목록 조회 시 성공")
    void getBoardsList_WithPagination_ShouldReturnCorrectPage() {
      // given
      List<Board> boards = Arrays.asList(new Board("세 번째 게시글", "세 번째 내용", 3L));
      boards.get(0).setId(3L);

      Pageable pageable = PageRequest.of(1, 2); // 두 번째 페이지, 페이지당 2개
      Page<Board> boardPage = new PageImpl<>(boards, pageable, 5L); // 총 5개 게시글

      when(boardRepository.findAll(pageable)).thenReturn(boardPage);

      // when
      Page<BoardListResponse> result = boardService.getBoardsList(pageable);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getContent()).hasSize(1);
      assertThat(result.getTotalElements()).isEqualTo(5);
      assertThat(result.getTotalPages()).isEqualTo(3); // 5개 게시글을 2개씩 나누면 3페이지
      assertThat(result.getNumber()).isEqualTo(1); // 현재 페이지는 1 (0-based)
      assertThat(result.getSize()).isEqualTo(2); // 페이지 크기는 2

      BoardListResponse board = result.getContent().get(0);
      assertThat(board.id()).isEqualTo(3L);
      assertThat(board.title()).isEqualTo("세 번째 게시글");
      assertThat(board.content()).isEqualTo("세 번째 내용");
      assertThat(board.memberId()).isEqualTo(3L);

      verify(boardRepository).findAll(pageable);
    }

    @Test
    @DisplayName("단일 게시글로 목록 조회 시 성공")
    void getBoardsList_WithSingleBoard_ShouldReturnSinglePage() {
      // given
      List<Board> boards = Arrays.asList(savedBoard);
      Pageable pageable = PageRequest.of(0, 10);
      Page<Board> boardPage = new PageImpl<>(boards, pageable, 1L);

      when(boardRepository.findAll(pageable)).thenReturn(boardPage);

      // when
      Page<BoardListResponse> result = boardService.getBoardsList(pageable);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getContent()).hasSize(1);
      assertThat(result.getTotalElements()).isEqualTo(1);
      assertThat(result.getTotalPages()).isEqualTo(1);

      BoardListResponse board = result.getContent().get(0);
      assertThat(board.id()).isEqualTo(1L);
      assertThat(board.title()).isEqualTo("테스트 게시글");
      assertThat(board.content()).isEqualTo("테스트 게시글 내용입니다.");
      assertThat(board.memberId()).isEqualTo(1L);

      verify(boardRepository).findAll(pageable);
    }
  }

  @Nested
  @DisplayName("게시글 수정 테스트")
  class UpdateBoardTest {

    @Test
    @DisplayName("유효한 정보로 게시글 수정 시 성공")
    void updateBoard_WithValidData_ShouldSucceed() {
      // given
      Long boardId = 1L;
      String newTitle = "수정된 제목";
      String newContent = "수정된 내용";
      Long memberId = 1L;

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      Board result = boardService.updateBoard(boardId, newTitle, newContent, memberId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getId()).isEqualTo(1L);
      assertThat(result.getTitle()).isEqualTo(newTitle);
      assertThat(result.getContent()).isEqualTo(newContent);
      assertThat(result.getMemberId()).isEqualTo(1L);
      verify(boardRepository).findById(boardId);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("존재하지 않는 게시글 ID로 수정 시 예외 발생")
    void updateBoard_WithNonExistingId_ShouldThrowException() {
      // given
      Long nonExistingId = 999L;
      String newTitle = "수정된 제목";
      String newContent = "수정된 내용";
      Long memberId = 1L;

      when(boardRepository.findById(nonExistingId)).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(
              () -> boardService.updateBoard(nonExistingId, newTitle, newContent, memberId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Board not found");
      verify(boardRepository).findById(nonExistingId);
    }

    @Test
    @DisplayName("작성자가 아닌 사용자가 게시글 수정 시 예외 발생")
    void updateBoard_WithDifferentMemberId_ShouldThrowException() {
      // given
      Long boardId = 1L;
      String newTitle = "수정된 제목";
      String newContent = "수정된 내용";
      Long differentMemberId = 2L; // 다른 사용자 ID

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));

      // when & then
      assertThatThrownBy(
              () -> boardService.updateBoard(boardId, newTitle, newContent, differentMemberId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("You are not the author of this post.");
      verify(boardRepository).findById(boardId);
    }

    @Test
    @DisplayName("null ID로 게시글 수정 시 예외 발생")
    void updateBoard_WithNullId_ShouldThrowException() {
      // given
      String newTitle = "수정된 제목";
      String newContent = "수정된 내용";
      Long memberId = 1L;

      when(boardRepository.findById(null)).thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> boardService.updateBoard(null, newTitle, newContent, memberId))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Board not found");
      verify(boardRepository).findById(null);
    }

    @Test
    @DisplayName("빈 제목으로 게시글 수정 시 성공")
    void updateBoard_WithEmptyTitle_ShouldSucceed() {
      // given
      Long boardId = 1L;
      String newTitle = "";
      String newContent = "수정된 내용";
      Long memberId = 1L;

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      Board result = boardService.updateBoard(boardId, newTitle, newContent, memberId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getTitle()).isEqualTo("");
      assertThat(result.getContent()).isEqualTo(newContent);
      verify(boardRepository).findById(boardId);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("빈 내용으로 게시글 수정 시 성공")
    void updateBoard_WithEmptyContent_ShouldSucceed() {
      // given
      Long boardId = 1L;
      String newTitle = "수정된 제목";
      String newContent = "";
      Long memberId = 1L;

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      Board result = boardService.updateBoard(boardId, newTitle, newContent, memberId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getTitle()).isEqualTo(newTitle);
      assertThat(result.getContent()).isEqualTo("");
      verify(boardRepository).findById(boardId);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("긴 제목으로 게시글 수정 시 성공")
    void updateBoard_WithLongTitle_ShouldSucceed() {
      // given
      Long boardId = 1L;
      String longTitle = "이것은 매우 긴 게시글 제목입니다. 최대 100자까지 입력할 수 있는 제목입니다.";
      String newContent = "수정된 내용";
      Long memberId = 1L;

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      Board result = boardService.updateBoard(boardId, longTitle, newContent, memberId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getTitle()).isEqualTo(longTitle);
      assertThat(result.getContent()).isEqualTo(newContent);
      verify(boardRepository).findById(boardId);
      verify(boardRepository).save(any(Board.class));
    }

    @Test
    @DisplayName("긴 내용으로 게시글 수정 시 성공")
    void updateBoard_WithLongContent_ShouldSucceed() {
      // given
      Long boardId = 1L;
      String newTitle = "수정된 제목";
      String longContent = "이것은 매우 긴 게시글 내용입니다. ".repeat(20) + "최대 1000자까지 입력할 수 있습니다.";
      Long memberId = 1L;

      when(boardRepository.findById(boardId)).thenReturn(Optional.of(savedBoard));
      when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

      // when
      Board result = boardService.updateBoard(boardId, newTitle, longContent, memberId);

      // then
      assertThat(result).isNotNull();
      assertThat(result.getTitle()).isEqualTo(newTitle);
      assertThat(result.getContent()).isEqualTo(longContent);
      verify(boardRepository).findById(boardId);
      verify(boardRepository).save(any(Board.class));
    }
  }
}
