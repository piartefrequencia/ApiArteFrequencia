package com.br.artefrequencia.ApiArteFrequencia.repository.Db1;

import org.springframework.data.jpa.repository.JpaRepository;
import com.br.artefrequencia.ApiArteFrequencia.model.Db1.ResponsavelChat;
import java.util.List;

public interface RepositoryResponsavelChat extends JpaRepository<ResponsavelChat, Long>{

    List<ResponsavelChat> findByAlunoIdAndAtivoTrue(Long alunoId);
    
}
