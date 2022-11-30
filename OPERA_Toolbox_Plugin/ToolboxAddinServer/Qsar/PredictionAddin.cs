using System.Collections.Generic;
using System.Linq;
using Toolbox.Docking.Api.Chemical;
using Toolbox.Docking.Api.Data;
using Toolbox.Docking.Api.Objects;
using Toolbox.Docking.Api.Objects.Qsar;

namespace OperaAddin.Qsar
{
    public class PredictionAddin : ITbPrediction
    {
        public PredictionAddin(TbData value, TbMetadata metadata, IReadOnlyDictionary<TbObjectId, TbData> xDescriptorsValues, IReadOnlyList<ISuportingChemicals> suportingChemicals)
        {
            Value = value;
            Metadata = metadata;
            XDescriptorsValues = xDescriptorsValues.ToDictionary(kvp => kvp.Key, kvp => kvp.Value);
        }

        public TbData Value { get; }

        public TbMetadata Metadata { get; }

        public IReadOnlyDictionary<TbObjectId, TbData> XDescriptorsValues { get; }

        public IReadOnlyList<ISuportingChemicals> SuportingChemicals  { get; }
    }
}
