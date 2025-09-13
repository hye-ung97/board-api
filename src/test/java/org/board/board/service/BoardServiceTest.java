package org.board.board.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.board.board.dto.board.BoardCreateRequest;
import org.board.board.dto.board.BoardCreateResponse;
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
}
