package org.appitcompany.kuimakulak.mapper;

import org.appitcompany.kuimakulak.dto.contributorDto.ContributorsResponse;
import org.appitcompany.kuimakulak.entity.Contributor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContributorMapper {
    ContributorsResponse toDto(Contributor contributor);
}
