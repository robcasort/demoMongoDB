package com.alca.repository.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.alca.controller.exceptions.PersonaNotFoundException;
import com.alca.model.Persona;
import com.alca.repository.PersonaRepository;
import com.alca.service.PersonaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PersonaServiceTest {
	@InjectMocks
    private PersonaServiceImpl personaService;

    @Mock
    private PersonaRepository personaRepository;
    
    @Test
    @DisplayName("Validar el registro de una Persona")
    void should_save_one_Persona() {
        // Given
        final var personaToSave = Persona.builder()
        		.nombre("Maria")
        		.apellidos("Valdez")
        		.edad(25)
        		.email("mvaldez@gmail.com")
        		.build();
        
        when(personaRepository.save(any(Persona.class))).thenReturn(personaToSave);

        // When
        final var actual = personaService.save(new Persona());

        // Then	
        assertThat(actual).usingRecursiveComparison().isEqualTo(personaToSave);
        assertNotNull(actual);
        verify(personaRepository, times(1)).save(any(Persona.class));
        verifyNoMoreInteractions(personaRepository);
    }
    
    @Test
    @DisplayName("Validar la busqueda de una Persona")
    void should_find_and_return_one_Persona() {
        // Given
        final var expectedPersona = Persona.builder()
        		.nombre("Jimmy")
        		.apellidos("Araos")
        		.edad(28)
        		.email("jaraos@gmail.com")
        		.build();
                
        when(personaRepository.findById(anyString())).thenReturn(Optional.of(expectedPersona));

        // When
        final var actual = personaService.findById(getRandomString());

        // Then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPersona);
        assertNotNull(actual);
        verify(personaRepository, times(1)).findById(anyString());
        verifyNoMoreInteractions(personaRepository);
    }
    
    @Test
    @DisplayName("Validar que no encuentre una persona que no existe")
    void should_not_found_a_Person_that_doesnt_exists() {
        // Given
        when(personaRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PersonaNotFoundException.class, () -> personaService.findById(getRandomString()));
        verify(personaRepository, times(1)).findById(anyString());
        verifyNoMoreInteractions(personaRepository);
    }
    
    @Test
    @DisplayName("Validar que se devuelva todas las personas")
    void should_find_and_return_all_persons() {
        // Given
        when(personaRepository.findAll()).thenReturn(List.of(new Persona(), new Persona()));

        // When & Then
        assertThat(personaService.findAll()).hasSize(2);
        verify(personaRepository, times(1)).findAll();
        verifyNoMoreInteractions(personaRepository);
    }        
    
    @Test
    @DisplayName("Validar la eliminacion del registro de una persona")
    void should_delete_one_person() {
        // Given
        doNothing().when(personaRepository).deleteById(anyString());

        // When
        personaService.deleteById(getRandomString());
        
        // Then
        verify(personaRepository, times(1)).deleteById(anyString());
        verifyNoMoreInteractions(personaRepository);
    }
    
    private String getRandomString() {
        return String.valueOf(new Random().ints(1, 10).findFirst().getAsInt());
    }
}
