package com.dintal.react.repository.rowmapper;

import com.dintal.react.domain.AIE;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link AIE}, with proper type conversions.
 */
@Service
public class AIERowMapper implements BiFunction<Row, String, AIE> {

    private final ColumnConverter converter;

    public AIERowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link AIE} stored in the database.
     */
    @Override
    public AIE apply(Row row, String prefix) {
        AIE entity = new AIE();
        entity.setId(converter.fromRow(row, prefix + "_id", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setType(converter.fromRow(row, prefix + "_type", String.class));
        entity.setDescription(converter.fromRow(row, prefix + "_description", String.class));
        entity.setCreatedAt(converter.fromRow(row, prefix + "_created_at", ZonedDateTime.class));
        entity.setCreatedBy(converter.fromRow(row, prefix + "_created_by", String.class));
        entity.setIcon(converter.fromRow(row, prefix + "_icon", String.class));
        entity.setVersion(converter.fromRow(row, prefix + "_version", String.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", String.class));
        entity.setRate(converter.fromRow(row, prefix + "_rate", Double.class));
        entity.setAieMetadata(converter.fromRow(row, prefix + "_aie_metadata", String.class));
        entity.setUserID(converter.fromRow(row, prefix + "_user_id", String.class));
        entity.setIsPublic(converter.fromRow(row, prefix + "_is_public", Boolean.class));
        entity.setOrganizationName(converter.fromRow(row, prefix + "_organization_name", String.class));
        entity.setTenantID(converter.fromRow(row, prefix + "_tenant_id", String.class));
        return entity;
    }
}
