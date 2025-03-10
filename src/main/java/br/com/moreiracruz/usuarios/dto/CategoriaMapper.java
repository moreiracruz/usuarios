//package br.com.moreiracruz.mapper;
//
//import br.com.moreiracruz.dto.CategoriaDTO;
//import br.com.moreiracruz.model.Categoria;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.factory.Mappers;
//
//@Mapper(componentModel = "spring", imports = {LocalDateTime.class}
//public interface CategoriaMapper {
//
//    CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "criacao", expression = "java(java.time.LocalDateTime.now())")
//    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
//    @Mapping(target = "horaCriacao", expression = "java(java.time.LocalTime.now())")
//    Categoria toEntity(CategoriaDTO dto);
//
//    @Mapping(target = "valor", numberFormat = "#0.00")
//    CategoriaDTO toDto(Categoria entity);
//
//    default String formatarData(LocalDateTime data) {
//    return data.format(DateTimeFormatter.ISO_LOCAL_DATE);
//}