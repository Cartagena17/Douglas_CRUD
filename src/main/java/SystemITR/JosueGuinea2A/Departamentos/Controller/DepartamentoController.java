package SystemITR.JosueGuinea2A.Departamentos.Controller;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Service.DepartamentosService;
import SystemITR.JosueGuinea2A.Response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/departamentos")
@CrossOrigin
public class DepartamentoController {


    /**
     * Inyectando la capa service sobre el controller
     */
    private final DepartamentosService service;

    public DepartamentoController(DepartamentosService service) {
        this.service = service;
    }

    /**
     * Nuevo recursos : Ingresar información -> POST
     * Obtener recursos: GET
     * Actualizar recursos: PUT / PATCH
     * Eliminar recursos: DELETE
     */
    @PostMapping
    public ResponseEntity<ApiResponse<DepartamentoDTO>> nuevoDepartamento(@Valid @RequestBody DepartamentoDTO json){
        try {
            DepartamentoDTO dto = service.nuevoDepartamento(json);
            if (dto != null) {
                log.info("Nuevo Departamento registrado" + dto);
                ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(true, "Datos ingresados exitosamente", dto);
                return ResponseEntity.ok(respuesta);
            }
            log.warn("Intento de inserción fallida: "+json);
            ApiResponse<DepartamentoDTO> respuestaFallida = new ApiResponse<>(false,"Intento fallido de insercion", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaFallida);

        }catch (Exception e){
            log.error("El proceso presentó un fallo inesperado, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", json);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentoDTO>>> obtenerDatos(){
        try {
            List<DepartamentoDTO> lista = service.obtenerTodo();
            if (lista != null){
                log.info("Datos de departamentos consultados");
                ApiResponse<List<DepartamentoDTO>> respuestaExito = new ApiResponse<>(true, "Datos encontrados", lista);
                return ResponseEntity.ok(respuestaExito);
            }
            log.info("Datos no encontrados");
            ApiResponse<List<DepartamentoDTO>> respuestaNoEncontrada = new ApiResponse<>(true, "Datos encontrados", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(respuestaNoEncontrada);
        } catch (Exception e) {
            log.error("El proceso presentó un fallo inesperado, consulte con el administrador");
            e.printStackTrace();
            ApiResponse<List<DepartamentoDTO>> respuesta = new ApiResponse<>(false, "El proceso no se pudo completar", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> obtenerDepartamentoPorId(@PathVariable Long id){
        try{
            DepartamentoDTO dto = service.buscarDepartamento(id);
            if(dto != null){
                log.info("Se obtuvieron los datos del departamento: " + id);
                ApiResponse<DepartamentoDTO> exito = new ApiResponse<>(true, "Se obtuvieron los datos del departamento: " + id, dto);
                return ResponseEntity.ok(exito);
            }
            log.info("Datos no encontrados con id: " + id);
            ApiResponse<DepartamentoDTO> respuestaNoEncontrada = new ApiResponse<>(false,"Datos no encontrados con id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }
        catch (Exception e){
            log.error("Error crítico al obtner el departamento con id: " + id);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> error  = new ApiResponse<>(false, "Error critico al obtener el departamento con id: " + id);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarDepartamento(@PathVariable Long id){
        try{
            boolean respuesta = service.eliminarDepartamento(id);
            if (respuesta){
                log.info("Departamento con id: " + id + " eliminado");
                ApiResponse<Void> exito = new ApiResponse<>(true, "Departamento con id: " + id + " eliminado");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(exito);
            }
            log.info("Departamento con id: " + id + ", no fue encontrado");
            ApiResponse<Void> respuestaNoEncontrada = new ApiResponse<>(false, "Departamento con id: " + id + ", no fue encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);
        }
        catch (Exception e){
            log.error("Error crítico, al eliminar el departamento con id: " + id);
            e.printStackTrace();
            ApiResponse<Void> error  = new ApiResponse<>(false, "Error critico, al eliminar el departamento con id: " + id);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> actualizarDATA (@PathVariable Long id, @Valid @RequestBody DepartamentoDTO dto){
        try {
            DepartamentoDTO data = service.actualizarData(id, dto);
            if (data != null){
                log.info("Departamento con id "+id+" ha sido actualizado");
                ApiResponse<DepartamentoDTO> respuestaExitosa = new ApiResponse<>(true, "Departamento con id "+id+" ha sido actualizado", data);
                return ResponseEntity.ok(respuestaExitosa);
            }
            log.warn("No se pudo actualizar departamento con id" +id);
            ApiResponse<DepartamentoDTO> respuestaNoCompletada = new ApiResponse<>(false,"No se pudo actualizar departamento con id" +id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuestaNoCompletada);
        } catch (Exception e) {
            log.error("Error crítico al actualizar el departamento con id " + id);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> error  = new ApiResponse<>(false, "Error crítico al actualizar el departamento con id " + id);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/abreviatura/{abreviatura}")
    public ResponseEntity<ApiResponse<DepartamentoDTO>> buscarPorAbreviatura (@PathVariable String abreviatura){
        try {
            DepartamentoDTO data = service.buscarAbreviatura(abreviatura);
            if (data != null){
                log.info("Departamento encontrado con abreviatura" + abreviatura);
                ApiResponse<DepartamentoDTO> exito = new ApiResponse<>(true, "Departamento encontrado con abreviatura " + abreviatura, data);
                return ResponseEntity.ok(exito);
            }
            log.warn("Departamento no encontrado "+abreviatura);
            ApiResponse<DepartamentoDTO> respuestaNoEncontrada = new ApiResponse<>(false,"Departamento no encontrado "+abreviatura );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuestaNoEncontrada);

        }catch (Exception e){
            log.error("Error crítico al obtner el departamento con abreviatura: " + abreviatura);
            e.printStackTrace();
            ApiResponse<DepartamentoDTO> error  = new ApiResponse<>(false, "Error critico al obtener el departamento con abreviatura " + abreviatura);
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
