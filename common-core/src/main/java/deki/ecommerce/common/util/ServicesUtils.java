package deki.ecommerce.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

public final class ServicesUtils {

    /**
     * Hide constructor.
     */
    private ServicesUtils() {
    }

    /**
     * Validate parameter not mull.
     *
     * @param parameter   Object
     * @param nullMessage String
     */
    public static void validateParameterNotNull(final Object parameter, final String nullMessage) {
        Preconditions.checkArgument(parameter != null, nullMessage);
    }

    /**
     * Validate parameter not null with standard message.
     *
     * @param parameter      String
     * @param parameterValue Object
     */
    public static void validateParameterNotNullStandardMessage(final String parameter, final Object parameterValue) {
        validateParameterNotNull(parameterValue, String.format("Parameter %s can not be null", parameter));
    }

    /**
     * Validate parameter not empty with standard message.
     *
     * @param parameter      String
     * @param parameterValue Object
     */
    public static void validateParameterNotEmptyStandardMessage(final String parameter, final Object parameterValue) {
        validateParameterNotEmpty(parameterValue, String.format("Parameter %s can not be empty", parameter));
    }

    /**
     * Validate parameter not empty.
     *
     * @param parameter    Object
     * @param emptyMessage String
     */
    public static void validateParameterNotEmpty(final Object parameter, final String emptyMessage) {
        if (parameter instanceof Collection) {
            Preconditions.checkArgument(CollectionUtils.isNotEmpty((Collection<?>) parameter), emptyMessage);
        } else if (parameter instanceof Map) {
            Preconditions.checkArgument(MapUtils.isNotEmpty((Map<?, ?>) parameter), emptyMessage);
        } else if (parameter instanceof String) {
            Preconditions.checkArgument(StringUtils.isNotEmpty((CharSequence) parameter), emptyMessage);
        } else {
            throw new IllegalArgumentException("Empty check is not support for " + parameter.getClass().getName());
        }
    }
}
