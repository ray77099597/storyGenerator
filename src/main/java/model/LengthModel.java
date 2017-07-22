package model;

import generator.Parameter;

public class LengthModel extends ModelAbstract{

	@Override
	public double getValue(Object... args) {
		if(args.length != 2) return 0.0;
		int length = (Integer)args[0];
		int desireLength = (Integer)args[1];
		return (double)Parameter.gaussianDistribution.value(length) / Parameter.gaussianDistribution.value(desireLength);
	}

}
