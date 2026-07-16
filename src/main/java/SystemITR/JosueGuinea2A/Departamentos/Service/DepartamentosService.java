package SystemITR.JosueGuinea2A.Departamentos.Service;

import SystemITR.JosueGuinea2A.Departamentos.DTO.DepartamentoDTO;
import SystemITR.JosueGuinea2A.Departamentos.Entity.DepartamentosEntity;
import SystemITR.JosueGuinea2A.Departamentos.Repository.DepartamentosRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DepartamentosService {

    //Forma 1 de inyección de dependencias y la mas recomendada
    private final DepartamentosRepository repo;
    public DepartamentosService(DepartamentosRepository repo){
        this.repo = repo;
    }

    public DepartamentoDTO nuevoDepartamento(@Valid DepartamentoDTO dto){
        try{
            //1. Convertir a Entity
            DepartamentosEntity entity = convertirAEntity(dto);
            //2. Guardar en la base de datos
            DepartamentosEntity entitySave = repo.save(entity);
            //3. Devolver una respuesta
            return convertirADTO(entitySave);
        }catch (Exception e){
            log.error("Error al ingresar la información del departamento" + e.getMessage());
            return null;
        }
    }

    private DepartamentosEntity convertirAEntity(@Valid DepartamentoDTO dto) {
        DepartamentosEntity objEntity = new DepartamentosEntity();
        objEntity.setNombreDepto(dto.getNombreDepto());
        objEntity.setAbreviatura(dto.getAbreviatura());
        objEntity.setUbicacion(dto.getUbicacion());
        return objEntity;
    }

    private DepartamentoDTO convertirADTO(@Valid DepartamentosEntity entity){
        DepartamentoDTO objDTO = new DepartamentoDTO();
        objDTO.setId(entity.getId());
        objDTO.setNombreDepto(entity.getNombreDepto());
        objDTO.setAbreviatura(entity.getAbreviatura());
        objDTO.setUbicacion(entity.getUbicacion());
        return objDTO;
    }

    public List<DepartamentoDTO> obtenerTodo() {
        List<DepartamentosEntity> data = repo.findAll();
        return data.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public DepartamentoDTO buscarDepartamento(Long id) {
        Optional<DepartamentosEntity> entidadOpcional = repo.findById(id);
        return entidadOpcional.map(this::convertirADTO).orElse(null);
    }

    public boolean eliminarDepartamento(Long id) {
        if (repo.existsById(id)){
            repo.deleteById(id);
            return true;
        }
        return false;
    }


    public DepartamentoDTO actualizarData(Long id, @Valid DepartamentoDTO dto) {

        try {
            //Buscar si el departamento existe por su id
            Optional<DepartamentosEntity> registroExistente = repo.findById(id);
            //Verificar si el objeto contiene valores (con if)
            if (registroExistente.isPresent()){
                //Creamos un objeto de tipo Entity
                DepartamentosEntity entidad = registroExistente.get();
                //Convertir y asiganar los dtos (nuevos valores) a la entidad
                entidad.setNombreDepto(dto.getNombreDepto());
                entidad.setAbreviatura(dto.getAbreviatura());
                entidad.setUbicacion(dto.getUbicacion());
                //Actualizar los datos en la bd
                DepartamentosEntity datosGuardados = repo.save(entidad);
                //Retornar la data convertida a DTO de forma previa
                return convertirADTO(datosGuardados);
            }
            //Si no trae datos retornar null
            return null;
        } catch (Exception e) {
            log.error("Ooop, ocurrio un error al procesar la informacion");
            return null;
        }
    }

    public DepartamentoDTO buscarAbreviatura(String abreviatura) {
        try {
            Optional<DepartamentosEntity> registro = repo.findByAbreviatura(abreviatura);
            if (registro.isPresent()){
                return convertirADTO(registro.get());
            }
            log.error("no existe ningun departamento con abreviatura" +abreviatura);
            return null;
        } catch (Exception e) {
            log.error("Ocurrio un error durante el proceso");
            return null;
        }
    }
}
