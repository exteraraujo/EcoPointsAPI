package br.com.ecopoints.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.com.ecopoints.api.model.AcaoSustentavel;
import java.util.List;

public interface AcaoSustentavelRepository extends JpaRepository<AcaoSustentavel, Long> {

	// Busca todas as ações de um usuário pelo id
	List<AcaoSustentavel> findByUsuarioId(Long usuarioId);

	// Soma os pontos de um usuário (pode retornar null se não houver ações)
	@Query("SELECT COALESCE(SUM(a.pontos), 0) FROM AcaoSustentavel a WHERE a.usuario.id = :usuarioId")
	Integer sumPontosByUsuarioId(@Param("usuarioId") Long usuarioId);
}
