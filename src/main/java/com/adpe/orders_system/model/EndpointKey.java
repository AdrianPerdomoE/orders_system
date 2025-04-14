package com.adpe.orders_system.model;

import java.util.Objects;

import org.springframework.util.AntPathMatcher;

import com.mongodb.lang.Nullable;

import lombok.Data;

@Data
public class EndpointKey  implements Comparable<EndpointKey> {
    private final String method; // Puede ser null para aplicar a cualquier método
    private final String path;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher(); // Instancia reutilizable

    public EndpointKey(@Nullable String method, String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("El path no puede ser null o vacío.");
        }

        this.method = (method != null) ? method.toUpperCase() : null; // Normaliza el método HTTP
        this.path = path; // Normaliza el path
    }
    public EndpointKey(String path) {
        this(null, path); // Llama al constructor con método null
    }

   

    /**
     * Verifica si este EndpointKey coincide con un método y un path específicos.
     *
     * @param requestMethod El método HTTP de la solicitud.
     * @param requestPath   El path de la solicitud.
     * @return true si coincide, false en caso contrario.
     */
    public boolean matches(String requestMethod, String requestPath) {
        // Verifica si el método HTTP coincide (null es un comodín para cualquier método)
        boolean methodMatches = (method == null || method.equalsIgnoreCase(requestMethod));

        // Usa AntPathMatcher para verificar si el path coincide
        boolean pathMatches = PATH_MATCHER.match(this.path, requestPath);

        return methodMatches && pathMatches;
    }
    // equals y hashCode necesarios para usar como key en Map
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EndpointKey)) return false;
        EndpointKey that = (EndpointKey) o;

        // Si el método es null, lo consideramos un comodín que coincide con cualquier método
        boolean methodEquals = (this.method == null || that.method == null) || Objects.equals(this.method, that.method);
        return methodEquals && Objects.equals(this.path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
       /**
     *método compareTo para comparar EndpointKey por especificidad.
     * - Menos comodines = mayor prioridad.
     * - Más segmentos = mayor prioridad en caso de empate.
     * - Métodos específicos (no null) tienen mayor prioridad.
     */
    @Override
    public int compareTo(EndpointKey other) {
        int wildcardCountThis = countWildcards(this.path);
        int wildcardCountOther = countWildcards(other.path);

        // Comparar por número de comodines (menos comodines = mayor prioridad)
        if (wildcardCountThis != wildcardCountOther) {
            return Integer.compare(wildcardCountOther, wildcardCountThis); // Invertido para priorizar menos comodines
        }

        int segmentCountThis = countSegments(this.path);
        int segmentCountOther = countSegments(other.path);

        // Comparar por número de segmentos (más segmentos = mayor prioridad)
        if (segmentCountThis != segmentCountOther) {
            return Integer.compare(segmentCountOther, segmentCountThis);
        }

        // Comparar por método (específico > general)
        boolean thisHasMethod = this.method != null;
        boolean otherHasMethod = other.method != null;
        if (thisHasMethod != otherHasMethod) {
            return thisHasMethod ? -1 : 1; // Específico tiene mayor prioridad
        }

        // Si todo es igual, son equivalentes
        return 0;
    }

    /**
     * Cuenta los comodines en el path (por ejemplo, ** o {param}).
     */
    private int countWildcards(String path) {
        return path.split("\\*\\*|\\{[^/]+\\}").length - 1;
    }

    /**
     * Cuenta la cantidad de segmentos en un path separados por '/'.
     */
    private int countSegments(String path) {
        return path.split("/").length;
    }
}