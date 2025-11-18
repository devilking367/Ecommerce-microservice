package deki.ecommerce.common.handler;

import deki.ecommerce.common.dto.response.Pageable;
import deki.ecommerce.common.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RequiredArgsConstructor
@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    //private final SpringDocConfigProperties springDocConfigProperties;
    //private final PlatformProperties platformProperties;

    @Override
    public boolean supports(@NonNull MethodParameter returnType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        // 1️⃣ Bỏ qua Swagger API docs
       /* if (springDocConfigProperties.getApiDocs().getPath()
                .equals(((ServletServerHttpRequest) request).getServletRequest().getRequestURI())) {
            return body;
        }*/

        // 2️⃣ Bỏ qua các endpoint được cấu hình trong properties
       /* List<String> ignoredWrapperEndpoints = platformProperties.getResponse().getIgnoredWrapperEndpoints();
        if (CollectionUtils.isNotEmpty(ignoredWrapperEndpoints)
                && ignoredWrapperEndpoints.contains(((ServletServerHttpRequest) request).getServletRequest().getRequestURI())) {
            return body;
        }*/

        // 3️⃣ Nếu body đã đúng format (Response, HATEOAS, file, v.v.) thì không wrap lại
        if (body instanceof Response
                //|| body instanceof RepresentationModel
                || body instanceof ByteArrayResource) {
            return body;
        }

        // 4️⃣ Nếu là Page<> thì wrap theo pagination chuẩn
        if (body instanceof Page<?> page) {
            return Response.builder()
                    .data(page.getContent())
                    .page(new Pageable(page.getTotalElements(), page.getNumber(), page.getSize(), page.getTotalPages()))
                    .build();
        }

        // 5️⃣ Các trường hợp còn lại thì gói vào Response chung
        return Response.builder().data(body).build();
    }
}

