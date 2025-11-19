package org.TPDesarrollo.Repository;

import org.TPDesarrollo.Clases.Huesped;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // 1. Esto arranca una base de datos en memoria (H2) solo para este test
class HuespedRepositoryTest {

    @Autowired
    private HuespedRepository huespedRepository;

    @Test
    void buscarHuespedesPorCriterios_DeberiaEncontrarPorApellido() {
        // Given (Preparamos la base de datos de prueba)
        Huesped h1 = new Huesped();
        h1.setNombre("Juan");
        h1.setApellido("Perez");
        h1.setDocumento("111");
        h1.setTelefono("123");

        Huesped h2 = new Huesped();
        h2.setNombre("Maria");
        h2.setApellido("Gomez");
        h2.setDocumento("222");
        h2.setTelefono("456");
        // ... llena los datos obligatorios ...

        huespedRepository.save(h1);
        huespedRepository.save(h2);

        // When (Ejecutamos tu Query personalizada)
        // Probamos buscar por "Perez" (debería traer 1)
        List<Huesped> resultados = huespedRepository.buscarHuespedesPorCriterios(
                "Perez", null, null, null
        );

        // Then (Verificamos)
        assertThat(resultados).hasSize(1);
        assertThat(resultados.getFirst().getNombre()).isEqualTo("Juan");
    }
}