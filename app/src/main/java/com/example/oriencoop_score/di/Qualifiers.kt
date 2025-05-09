package com.example.oriencoop_score.di
import javax.inject.Qualifier

/**
 * Los qualifiers sirven como una clase de identifidor único para que no existan problemas
 * con creación de múltiples servicios de retrofit con el mismo nombre.
 *
 * Actualmente no sé está ocupando (07/05/2025)
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosCredito

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosAhorro

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosLcc

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosLcr

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosCsocial

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisProductosDap

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisMovimientosAhorro

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisMovimientosCsocial

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisMovimientosLcr


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MisMovimientosCredito