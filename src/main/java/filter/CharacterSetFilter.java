package filter;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CharacterSetFilter")
public class CharacterSetFilter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        res.setContentType("application/json; charset=UTF-8");
        res.setCharacterEncoding("UTF-8");
        ((HttpServletResponse) res).setHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) res).setHeader("Access-Control-Allow-Methods", "*");
        ((HttpServletResponse) res).setHeader("Access-Control-Allow-Headers", "*");
        ((HttpServletResponse) res).setHeader("Access-Control-Max-Age", "86400");
        ((HttpServletResponse) res).setHeader("Allow", "GET, HEAD, POST, TRACE, OPTIONS, PUT, DELETE");
        chain.doFilter(req, res);
    }
}
