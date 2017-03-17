package testservices;

import org.junit.Assert;
import org.junit.experimental.theories.Theories;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


// Original from https://github.com/rgorodischer/fault-tolerant-theories/
// Modified by kos.gdk
public class FaultTolerantTheoriesRunner extends Theories {

	public FaultTolerantTheoriesRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	@Override
	public Statement methodBlock(FrameworkMethod method) {
		return new FaultTolerantAnchor(method, getTestClass());
	}

	class FaultTolerantAnchor extends TheoryAnchor {

		private final List<Throwable> failures = new LinkedList<>();
		private final String methodName;

		private int testsCount;

		FaultTolerantAnchor(FrameworkMethod method, TestClass testClass) {
			super(method, testClass);
			this.methodName = method.getName();
		}

		@Override
		public void evaluate() throws Throwable {
			super.evaluate();
			if (!failures.isEmpty()) {
				System.err.println("Theory '" + getTestClass().getJavaClass().getSimpleName() + "." + methodName + "' failed for " + failures.size() +
						" scenario" + (failures.size() > 1 ? "s" : "") + " out of " + testsCount + ":\n");
				for (Throwable failure : failures) {
					System.err.println((failures.indexOf(failure) + 1) + ". " + failure.getMessage());
				}
				Assert.fail("");
			}
		}

		@Override
		protected void reportParameterizedError(Throwable e, Object... params) throws Throwable {
			if (params.length == 0) {
				failures.add(e);
				e.printStackTrace();
			} else {
				Error error = new Error("Parameters: " + Arrays.toString(params), e);
				failures.add(error);
				error.printStackTrace();
			}
			handleDataPointSuccess();
		}

		@Override
		protected void handleDataPointSuccess() {
			super.handleDataPointSuccess();
			testsCount++;
		}

	}
}