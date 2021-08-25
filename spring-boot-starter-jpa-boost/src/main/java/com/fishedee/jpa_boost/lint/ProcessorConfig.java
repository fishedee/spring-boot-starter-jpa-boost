package com.fishedee.jpa_boost.lint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessorConfig {
    private Class superEntityClass;

    private Class superEmbeddableClass;

    private boolean allowIdHaveGeneratedValue;

    private Class<? extends JPALinter>[] extraLinters;
}
