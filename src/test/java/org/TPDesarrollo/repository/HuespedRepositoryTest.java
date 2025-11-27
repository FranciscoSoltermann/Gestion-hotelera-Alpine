package org.TPDesarrollo.repository;

import org.TPDesarrollo.clases.Huesped;
import org.TPDesarrollo.enums.RazonSocial;
import org.TPDesarrollo.enums.TipoDocumento; // Asegúrate de tener este import
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
// 👇 ESTA LÍNEA ES LA CLAVE: Le dice que use tu PostgreSQL real
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HuespedRepositoryTest {

    @Autowired
    private HuespedRepository huespedRepository;

    @Test
    void buscarHuespedesPorCriterios_DeberiaEncontrarPorApellido() {
        // 1. GIVEN: Preparamos datos de prueba
        // Creamos un huésped con TODOS los datos obligatorios para que Postgres no se queje
        Huesped h1 = new Huesped();
        h1.setNombre("Juan");
        h1.setApellido("PerezTest"); // Uso un apellido único para identificarlo fácil
        h1.setDocumento("999111");
        h1.setTipoDocumento(TipoDocumento.DNI);
        h1.setTelefono("111-111");
        h1.setEmail("juan_test@mail.com");
        h1.setPosicionIVA(RazonSocial.Consumidor_Final);

        // Creamos un segundo huésped para verificar que el filtro funciona
        Huesped h2 = new Huesped();
        h2.setNombre("Maria");
        h2.setApellido("GomezTest");
        h2.setDocumento("999222");
        h2.setTipoDocumento(TipoDocumento.DNI);
        h2.setTelefono("222-222");
        h2.setEmail("maria_test@mail.com");
        h2.setPosicionIVA(RazonSocial.Consumidor_Final);

        // Guardamos en tu base de datos real
        huespedRepository.save(h1);
        huespedRepository.save(h2);

        // 2. WHEN: Ejecutamos tu búsqueda personalizada
        // Buscamos solo por apellido "PerezTest"
        List<Huesped> resultados = huespedRepository.buscarHuespedesPorCriterios(
                "PerezTest", null, null, null
        );

        // 3. THEN: Verificamos
        assertThat(resultados).isNotEmpty();
        // Debería traer al menos a Juan (o más si ya tenías otros PerezTest en tu BD)
        assertThat(resultados.get(0).getApellido()).isEqualTo("PerezTest");
    }
}