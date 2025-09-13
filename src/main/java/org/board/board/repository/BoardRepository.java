package org.board.board.repository;

import java.util.List;

import org.board.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

  List<Board> findByMemberId(Long memberId);
}
