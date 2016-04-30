package nl.food4bees.backend.user;

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.UppercaseCharacterRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.AlphabeticalSequenceRule;
import edu.vt.middleware.password.NumericalSequenceRule;
import edu.vt.middleware.password.QwertySequenceRule;
import edu.vt.middleware.password.RepeatCharacterRegexRule;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.RuleResult;

import nl.food4bees.backend.Util;
import nl.food4bees.backend.Config;
import nl.food4bees.backend.Mailer;
import nl.food4bees.backend.TokenGenerator;

/**
 * A user registration servlet that uses POST.
 *
 * @author Alexander Feldman
 */

public class RegisterServlet extends HttpServlet
{
    private static String sourceClass = RegisterServlet.class.getName();
    private static Logger logger = Logger.getLogger(sourceClass);

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("repeat");

        if (Util.trim(name) == null) {
            error("Missing name", request, response);

            return;
        }

        if (Util.trim(email) == null) {
            error("Missing email", request, response);

            return;
        }

        if (Util.trim(password) == null) {
            error("Missing password", request, response);

            return;
        }

        if (!password.equals(passwordConfirmation)) {
            error("Passwords do not match", request, response);

            return;
        }

        // Check password strength.

        // Password must be between 8 and 16 chars long.
        LengthRule lengthRule = new LengthRule(8, 16);

        // Control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();

        // Require at least 1 digit in passwords
        charRule.getRules().add(new DigitCharacterRule(1));

        // Require at least 1 non-alphanumeric char
        charRule.getRules().add(new NonAlphanumericCharacterRule(1));

        // Require at least 1 upper case char
        charRule.getRules().add(new UppercaseCharacterRule(1));

        // Require at least 1 lower case char
        charRule.getRules().add(new LowercaseCharacterRule(1));

        // Require at least 3 of the previous rules be met
        charRule.setNumberOfCharacteristics(3);

        // Don't allow alphabetical sequences
        AlphabeticalSequenceRule alphaSeqRule = new AlphabeticalSequenceRule();

        // Don't allow numerical sequences of length 3
        NumericalSequenceRule numSeqRule = new NumericalSequenceRule(3, true);

        // Don't allow qwerty sequences
        QwertySequenceRule qwertySeqRule = new QwertySequenceRule();

        // Don't allow 4 repeat characters
        RepeatCharacterRegexRule repeatRule = new RepeatCharacterRegexRule(4);

        // Group all rules together in a List
        List<Rule> ruleList = new ArrayList<Rule>();

        ruleList.add(lengthRule);
        ruleList.add(charRule);
        ruleList.add(alphaSeqRule);
        ruleList.add(numSeqRule);
        ruleList.add(qwertySeqRule);
        ruleList.add(repeatRule);

        PasswordValidator validator = new PasswordValidator(ruleList);
        PasswordData passwordData = new PasswordData(new Password(password));

        RuleResult result = validator.validate(passwordData);
        if (!result.isValid()) {
            request.setAttribute("errors", validator.getMessages(result));
            error("Weak password:", request, response);

            return;
        }

        Integer uid = null;
        try {
            Database db = new Database();

            if (db.hasUser(email)) {
                error("This email is already registered", request, response);

                return;
            }

            Entry user = new Entry(null, name, email, password, new nl.food4bees.backend.group.Database().getId("User"));
            String verificationToken = TokenGenerator.getToken();

            db.startTransaction();
            try {
                Integer id = db.add(user);
                db.addVerificationToken(id, verificationToken);

                db.commit();
            } catch (Exception e) {
                db.rollback();

                throw e;
            } finally {
                db.endTransaction();
            }

            Map<String, String> params = new HashMap<String, String>();

            Config config = Config.instance();

            String secureURL = config.getProperty("secure_url");

            params.put("url", secureURL + "verify-registration?token=" + verificationToken);

            Mailer.instance().sendMail("validate_email",
                                       email,
                                       name,
                                       "webmaster@food4bees.nl",
                                       "The Food4Bees Web Administrator",
                                       params);

            request.setAttribute("caption", "Registration Completed Successfully");
            request.setAttribute("message",
                                 "Thank you for registering. " + 
                                 "In order to verify your identity, we have sent you an email.");
            request.setAttribute("destination", "login.jsp");
            request.getRequestDispatcher("message.jsp").forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Received a database exception.", e);

            error("Internal error", request, response);

            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Received an unexpected exception.", e);

            error("Internal error", request, response);

            return;
        }
    }

    private void preserveParameters(HttpServletRequest request)
    {
        request.setAttribute("name", request.getParameter("name"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("password", request.getParameter("password"));
        request.setAttribute("repeat", request.getParameter("repeat"));
    }

    private void error(String error, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        preserveParameters(request);

        request.setAttribute("error", error);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
